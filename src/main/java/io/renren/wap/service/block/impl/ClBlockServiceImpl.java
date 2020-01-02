package io.renren.wap.service.block.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsClblockDaoImpl;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.service.BlockMsgSendService;
import io.renren.wap.service.MachineService;
import io.renren.wap.service.block.OperationKeyService;
import io.renren.wap.service.msg.ClMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 输送线逻辑处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/15  16:49
 * @Version: V1.0.0
 **/
public class ClBlockServiceImpl extends BlockService implements OperationKeyService {

    @Override
    public void operationKey(String blockName) throws InterruptedException {
        String key = null;
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        String plcName = machine.getPlcName();
        WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        if (isCanSendMsg(clBlock)) {
            //  佳田特殊输送线
            String blockName_0001="0001";
            if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)&&blockName_0001.equals(blockName)){
                clBlock.setIsLoad(true);
            }
            BlockMsgSendService blockMsgSendService = new BlockMsgSendService();
            int keyType = getKeyType(clBlock);
            if (BlockConstant.MCKEY_NOT_EMPTY == keyType || BlockConstant.KEY_NOT_EMPTY == keyType) {
                key = clBlock.getMckey();
            } else if (BlockConstant.APPOINTMENT_MCKEY_NOT_EMPTY == keyType) {
                key = clBlock.getAppointmentMckey();
                clBlock.setWithWorkBlockName(clBlock.getReserved1());
                clBlock.setMckey(key);
                clBlock.setAppointmentMckey("");
                WcsClblockDaoImpl.getInstance().updateThreeValueBlock(key, "", clBlock.getReserved1(), blockName);
            }
            if (StringUtils.isNotEmpty(key)) {
                MsgDTO msgDTO = doWork(key, clBlock, plcName);
                boolean resultSend = blockMsgSendService.sendMsg(msgDTO, blockName);
                if (resultSend) {
                    int resultUpdateDb = updateCommand(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, DbUtil.getCLBlockDao(), blockName);
                    Log4j2Util.getBlockBrickLogger().info(String.format("blockName:%s,mcKey:%s,修改指令 %s 结果 %d", blockName, key, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, resultUpdateDb));
                    blockMsgSendService.resendMsg(msgDTO, blockName);
                }
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
     * @param clBlock 输送线状态
     * @param plcName plc名称
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/15 17:38
     */
    private static MsgDTO doWork(String mcKey, WcsClblockEntity clBlock, String plcName) {
        MsgDTO msgDTO = null;
        ClMsgService clMsgService = new ClMsgService();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        Integer workPlanType = workPlan.getType();
        String blockName = clBlock.getName();
        String withWorkBlockName = clBlock.getWithWorkBlockName();
        if (MachineService.isClMachine(withWorkBlockName)) {
            if (clBlock.getIsLoad()) {
                msgDTO = clMsgService.transplantingTheUnloading(workPlanType, mcKey, blockName, withWorkBlockName, plcName);
            } else {
                msgDTO = clMsgService.transplantingPickUp(workPlanType, mcKey, blockName, withWorkBlockName, plcName);
            }
        } else {
            if (MachineService.isSameDock(blockName, withWorkBlockName)) {
                if (clBlock.getIsLoad()) {
                    msgDTO = clMsgService.transplantingTheUnloading(workPlanType, mcKey, blockName, withWorkBlockName, plcName);
                } else {
                    msgDTO = clMsgService.transplantingPickUp(workPlanType, mcKey, blockName, withWorkBlockName, plcName);
                }
            } else {
                Log4j2Util.getBlockBrickLogger().info(String.format("%s ，当前设备，withWorkBlockName：%s 位置不同", blockName, withWorkBlockName));
            }
        }
        return msgDTO;
    }
}
