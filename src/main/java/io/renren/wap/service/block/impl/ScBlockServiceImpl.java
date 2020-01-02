package io.renren.wap.service.block.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.BlockMsgSendService;
import io.renren.wap.service.block.OperationKeyService;
import io.renren.wap.service.block.charge.ChargeServiceInterface;
import io.renren.wap.service.block.charge.self.SelfScBlockServiceImpl;
import io.renren.wap.service.block.charge.shelf.ShelfScBlockServiceImpl;
import io.renren.wap.service.msg.ScMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 穿梭车逻辑处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/15  13:22
 * @Version: V1.0.0
 **/
public class ScBlockServiceImpl extends BlockService implements OperationKeyService {

    @Override
    public void operationKey(String blockName) throws InterruptedException {
        WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        if (isCanSendMsg(scBlock)) {
            BlockMsgSendService blockMsgSendService = new BlockMsgSendService();
            String key = scBlock.getMckey();
            String scBlockAppointmentMcKey = scBlock.getAppointmentMckey();
            if (StringUtils.isNotEmpty(key)) {
                MsgDTO msgDTO = doWork(key, scBlock);
                boolean resultSend = blockMsgSendService.sendMsg(msgDTO, blockName);
                if (resultSend) {
                    int resultUpdateDb = updateCommand(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, DbUtil.getSCBlockDao(), blockName);
                    Log4j2Util.getBlockBrickLogger().info(String.format("blockName:%s,修改指令 %s 结果 %d", blockName, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, resultUpdateDb));
                    blockMsgSendService.resendMsg(msgDTO, blockName);
                }
            } else if (StringUtils.isEmpty(key) && StringUtils.isNotEmpty(scBlockAppointmentMcKey)) {
                //  任务分配
                String withWorkBlockName = scBlock.getReserved1();
                WcsScblockDaoImpl.getInstance().updateThreeScBlock(scBlockAppointmentMcKey, "", withWorkBlockName, blockName);
                //  消息发送
                operationKey(blockName);
            } else {
                Log4j2Util.getBlockBrickLogger().info(String.format("%s，没有任务！", blockName));
            }
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s，当前设备不满足消息制作条件！", blockName));
        }
    }

    /**
     * 逻辑处理
     *
     * @param mcKey   mcKey
     * @param scBlock 穿梭车状态
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/16 9:24
     */
    private MsgDTO doWork(String mcKey, WcsScblockEntity scBlock) {
        MsgDTO msgDTO = null;
        ScMsgService scMsgService = new ScMsgService();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        String fromStation = workPlan.getFromStation();
        String toStation = workPlan.getToStation();
        Integer workPlanType = workPlan.getType();
        String blockName = scBlock.getName();
        String hostBlockName = scBlock.getHostBlockName();
        String status = scBlock.getStatus();
        String withWorkBlockName = scBlock.getWithWorkBlockName();
        String row = scBlock.getRow();
        String line = scBlock.getLine();
        String tier = scBlock.getTier();
        String toLocation = workPlan.getToLocation();
        String fromLocation = workPlan.getFromLocation();
        if (!isScStatusCanWork(status, workPlanType)) {
            Log4j2Util.getBlockBrickLogger().info(String.format("当前设备状态及工作计划类型匹配，block：%s，status：%s，workPlanType：%d", blockName, status, workPlanType));
            return null;
        }
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
            if (StringUtils.isEmpty(hostBlockName)) {
                if (scBlock.getIsLoad()) {
                    if (isSameSite(row, line, tier, toLocation)) {
                        // 卸货
                        msgDTO = scMsgService.loadOff(mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                    }
                } else {
                    msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                }
            } else {
                if (scBlock.getIsLoad() && isSameSite(row, line, tier, toLocation)) {
                    // 载货下车
                    msgDTO = scMsgService.offCarLoad(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier);
                } else {
                    Log4j2Util.getBlockBrickLogger().info(String.format("%s，mcKey: %s，入库任务，状态不正确", blockName, mcKey));
                }
            }
        } else if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            String reserved2 = scBlock.getReserved2();
            if (BlockConstant.SC_PRIORITY_OFF_CAR_RESERVED2.equals(reserved2)) {
                if (StringUtils.isNotEmpty(scBlock.getHostBlockName())) {
                    msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, fromLocation);
                } else {
                    if (scBlock.getIsLoad()) {
                        if (isSameSiteOfTwoMachine(row, line, tier, scBlock.getWithWorkBlockName())) {
                            // 载货上车
                            msgDTO = scMsgService.getCarLoad(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier);
                        }
                    } else {
                        if (isSameArea(row, line, tier, fromLocation)) {
                            // 取货
                            msgDTO = scMsgService.pick(mcKey, workPlanType, blockName, fromLocation);
                        } else {
                            msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                        }
                    }
                }
            } else {
                if (StringUtils.isEmpty(scBlock.getHostBlockName())) {
                    if (scBlock.getIsLoad()) {
                        if (isSameSiteOfTwoMachine(row, line, tier, scBlock.getWithWorkBlockName())) {
                            // 载货上车
                            msgDTO = scMsgService.getCarLoad(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier);
                        }
                    } else {
                        if (isSameArea(row, line, tier, fromLocation)) {
                            // 取货
                            msgDTO = scMsgService.pick(mcKey, workPlanType, blockName, fromLocation);
                        } else {
                            msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                        }
                    }
                } else {
                    if (!scBlock.getIsLoad()) {
                        msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, fromLocation);
                    } else {
                        Log4j2Util.getBlockBrickLogger().info(String.format("%s，mcKey: %s，出库任务，状态不正确", blockName, mcKey));
                    }
                }
            }
        } else if (WorkPlanConstant.TYPE_MOVEMENT==workPlanType) {
            if (StringUtils.isEmpty(hostBlockName)) {
                if (scBlock.getIsLoad()) {
                    if (isSameSite(row, line, tier, fromLocation) && isSameSiteOfTwoMachine(row, line, tier, withWorkBlockName)) {
                        // 载货上车
                        msgDTO = scMsgService.getCarLoad(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier);
                    } else if (isSameArea(row, line, tier, toLocation)) {
                        // 卸货
                        msgDTO = scMsgService.loadOff(mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                    }
                } else {
                    if (isSameArea(row, line, tier, fromLocation)) {
                        // 取货
                        msgDTO = scMsgService.pick(mcKey, workPlanType, blockName, fromLocation);
                    } else {
                        msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                    }
                }
            } else {
                if (scBlock.getIsLoad()) {
                    if (isSameSite(row, line, tier, toLocation)) {
                        // 载货下车
                        msgDTO = scMsgService.offCarLoad(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier);
                    } else {
                        Log4j2Util.getBlockBrickLogger().info(String.format("%s，mcKey: %s，移库，与目标货位位置不同，row：%s，line：%s,tier: %s,toLocation: %s", blockName, mcKey, row, line, tier, toLocation));
                    }
                } else {
                    msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, fromLocation);
                }
            }
        } else if (WorkPlanConstant.TYPE_TALLY==workPlanType) {
            if (StringUtils.isEmpty(hostBlockName)) {
                if (isSameArea(row, line, tier, toLocation)) {
                    // 理货
                    msgDTO = scMsgService.tally(mcKey, workPlanType, blockName, row, line, tier);
                } else {
                    msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                }
            } else {
                msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, toLocation);
            }
        } else if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
            if (StringUtils.isEmpty(hostBlockName)) {
                if (BlockConstant.BERTH_BLOCK_NAME_LOCATION.equals(scBlock.getBerthBlockName())) {
                    msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                } else {
                    if (withWorkBlockName.contains(MachineConstant.TYPE_MC)) {
                        msgDTO = scMsgService.getCar(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier, MsgCycleOrderConstant.CYCLE_GO_MC_21);
                    } else {
                        msgDTO = scMsgService.getCar(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier, MsgCycleOrderConstant.CYCLE_GO_AL_20);
                    }
                }
            } else {
                if (hostBlockName.contains(MachineConstant.TYPE_AL)) {
                    msgDTO = scMsgService.offCar(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier, MsgCycleOrderConstant.CYCLE_GO_CL_B_19);
                } else {
                    if (hostBlockName.equals(toStation)){
                        String scBlockName=workPlan.getReserved2();
                        if (scBlockName.equals(scBlock.getName())){
                            msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, toLocation);
                        }else {
                            msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, "001001001");
                        }
                    }else {
                        if (blockName.equals(workPlan.getReserved2())){
                            msgDTO = scMsgService.offCar(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier, MsgCycleOrderConstant.CYCLE_GO_CL_A_18);
                        }else {
                            msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, "001001001");
                        }
                    }
                }
            }
        } else if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                ChargeServiceInterface chargeServiceInterface = new SelfScBlockServiceImpl();
                msgDTO = chargeServiceInterface.chargeUp(workPlan, scBlock);
            } else {
                ChargeServiceInterface shelfScBlockService = new ShelfScBlockServiceImpl();
                msgDTO = shelfScBlockService.chargeUp(workPlan, scBlock);
            }
        } else if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                ChargeServiceInterface chargeServiceInterface = new SelfScBlockServiceImpl();
                msgDTO = chargeServiceInterface.chargeFinish(workPlan, scBlock);
            } else {
                ChargeServiceInterface shelfScBlockService = new ShelfScBlockServiceImpl();
                msgDTO = shelfScBlockService.chargeFinish(workPlan, scBlock);
            }
        } else if (WorkPlanConstant.TYPE_TAKE_STOCK==workPlanType) {
            if (StringUtils.isEmpty(hostBlockName)) {
                if (isSameArea(row, line, tier, toLocation)) {
                    // 盘点
                    msgDTO = scMsgService.takeStock(mcKey, workPlanType, blockName, row, line, tier);
                } else {
                    msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
                }
            } else {
                msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, toLocation);
            }
        } else if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            if (StringUtils.isEmpty(hostBlockName)) {
                msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
            }
        } else if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
            if (StringUtils.isNotEmpty(hostBlockName)) {
                msgDTO = offCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName, toLocation);
            } else {
                WcsScblockDaoImpl.getInstance().updateMcKey("", blockName);
            }
        } else if (WorkPlanConstant.TYPE_GET_CAR==workPlanType) {
            if (StringUtils.isEmpty(hostBlockName)) {
                msgDTO = getCar(scMsgService, mcKey, workPlanType, blockName, row, line, tier, withWorkBlockName);
            }
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s，未识别的任务类型，mcKey：%s ，任务类型：%s", blockName, mcKey, Integer.toString(workPlanType)));
        }
        return msgDTO;
    }

    /**
     * 空车上车
     *
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/15 16:38
     */
    protected MsgDTO getCar(ScMsgService scMsgService, String mcKey, Integer workPlanType, String blockName, String row, String line, String tier, String withWorkBlockName) {
        MsgDTO msgDTO = null;
        if (isSameSiteOfTwoMachine(row, line, tier, withWorkBlockName)) {
            // 空车上车
            msgDTO = scMsgService.getCar(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier);
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s，mcKey: %s，设备与设备位置不同，sql数据：排：%s，列：%s，层：%s，withWorkBlockName：%s", blockName, row, line, tier, withWorkBlockName, mcKey));
        }
        return msgDTO;
    }

    /**
     * 空车下车
     *
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/15 16:38
     */
    protected MsgDTO offCar(ScMsgService scMsgService, String mcKey, Integer workPlanType, String blockName, String row, String line, String tier, String withWorkBlockName, String location) {
        MsgDTO msgDTO = null;
        if (isSameSite(row, line, tier, location)) {
            // 空车下车
            msgDTO = scMsgService.offCar(mcKey, workPlanType, blockName, withWorkBlockName, row, line, tier);
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s，mcKey: %s，设备与目标位置不同，sql数据：排：%s，列：%s，层：%s，location：%s", blockName, mcKey, row, line, tier, location));
        }
        return msgDTO;
    }

}
