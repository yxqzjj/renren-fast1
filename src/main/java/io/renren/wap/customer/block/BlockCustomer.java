package io.renren.wap.customer.block;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.*;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.BlockCache;
import io.renren.wap.cache.BlockQueueCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderAckDTO;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.MsgCycleOrderAckService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/1/23  14:51
 * @Version: V1.0.0
 **/
public class BlockCustomer implements Runnable, BlockInterface {
    private String blockName;

    BlockCustomer(String blockName) {
        this.blockName = blockName;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MsgDTO msgDTO = BlockQueueCache.getMsg(blockName);
                String command = msgDTO.getCommandType();
                String key;
                if (msgDTO instanceof MsgCycleOrderAckDTO) {
                    key = ((MsgCycleOrderAckDTO) msgDTO).getMcKey();
                    if (workPlanIsExcept(key)) {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s,mcKey: %s,block消息处理失败", blockName, key));
                        continue;
                    }
                    doMsgCycleOrderAckDTO((MsgCycleOrderAckDTO) msgDTO);
                } else if (msgDTO instanceof MsgCycleOrderFinishReportDTO) {
                    String cycleCommand = ((MsgCycleOrderFinishReportDTO) msgDTO).getCycleCommand();
                    if (!checkCycleCommand(blockName, cycleCommand)) {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s 检验指令不一致，35指令：%s,03指令：%s", blockName, cycleCommand, BlockCache.getString(blockName)));
                        if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
                            if (blockName.contains(MachineConstant.TYPE_SC)) {
                                WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                                if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_ACK.equals(scBlock.getCommand()) || MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER.equals(scBlock.getCommand())) {
                                    WcsScblockDaoImpl.getInstance().updateCommandByPrimaryKey(blockName, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
                                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                                }
                            }
                        }
                        continue;
                    }
                    doMsgCycleOrderFinishReportDTO((MsgCycleOrderFinishReportDTO) msgDTO);
                } else {
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s,block消息处理，未做解析的命令种类：%s", blockName, command));
                }
            } catch (Exception e) {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s,block消息处理异常，异常：%s", blockName, e.getMessage()));
            }
        }
    }

    /**
     * 检验动作是否一致
     *
     * @param blockName    数据block名称
     * @param cycleCommand 设备回复消息中cycle指令
     * @return boolean
     * @author CalmLake
     * @date 2019/3/6 16:38
     */
    private boolean checkCycleCommand(String blockName, String cycleCommand) {
        String cycleCommand_ = BlockCache.getString(blockName);
        if (cycleCommand_ == null) {
            return true;
        }
        return cycleCommand_.equals(cycleCommand);
    }

    /**
     * 工作计划是否正常
     *
     * @param key mcKey
     * @return boolean
     * @author CalmLake
     * @date 2019/2/21 15:33
     */
    private boolean workPlanIsExcept(String key) {
        try {
            WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",key));
            return WorkPlanConstant.STATUS_WORKING!=workPlan.getStatus() && WorkPlanConstant.STATUS_FINISH!=workPlan.getStatus() && WorkPlanConstant.STATUS_WAIT!=workPlan.getStatus();
        } catch (Exception e) {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s,block消费消息处理，判断工作计划状态异常：%s", key, e.getMessage()));
            return true;
        }
    }

    /**
     * cycle指示应答处理
     *
     * @param msgCycleOrderAckDTO cycle指示应答消息
     * @author CalmLake
     * @date 2019/1/23 14:49
     */
    private void doMsgCycleOrderAckDTO(MsgCycleOrderAckDTO msgCycleOrderAckDTO) {
        String blockName = msgCycleOrderAckDTO.getMachineName();
        String msgMcKey = msgCycleOrderAckDTO.getMcKey();
        String ackType = msgCycleOrderAckDTO.getAckType();
        String exceptionType = msgCycleOrderAckDTO.getExceptionType();
        String loadStatus = msgCycleOrderAckDTO.getLoadStatus();
        String command;
        String blockMcKey;
        String blockAppointmentMcKey;
        Object object = DbUtil.getBlockDao(blockName);
        if (object instanceof WcsClblockDaoImpl) {
            WcsClblockEntity block = ((WcsClblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = block.getCommand();
            blockMcKey = block.getMckey();
            blockAppointmentMcKey = block.getAppointmentMckey();
        } else if (object instanceof WcsAlblockDaoImpl) {
            WcsAlblockEntity block = ((WcsAlblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = block.getCommand();
            blockMcKey = block.getMckey();
            blockAppointmentMcKey = block.getAppointmentMckey();
        } else if (object instanceof WcsMlblockDaoImpl) {
            WcsMlblockEntity block = ((WcsMlblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = block.getCommand();
            blockMcKey = block.getMckey();
            blockAppointmentMcKey = block.getAppointmentMckey();
        } else if (object instanceof WcsMcblockDaoImpl) {
            WcsMcblockEntity block = ((WcsMcblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = block.getCommand();
            blockMcKey = block.getMckey();
            blockAppointmentMcKey = block.getAppointmentMckey();
        } else if (object instanceof WcsScblockDaoImpl) {
            WcsScblockEntity block = ((WcsScblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = block.getCommand();
            blockMcKey = block.getMckey();
            blockAppointmentMcKey = block.getAppointmentMckey();
        } else if (object instanceof WcsRgvblockDaoImpl) {
            WcsRgvblockEntity block = ((WcsRgvblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = block.getCommand();
            blockMcKey = block.getMckey();
            blockAppointmentMcKey = block.getAppointmentMckey();
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s,23处理，未解析的数据类型！", blockName));
            return;
        }
        boolean loadStorage;
        if (MsgCycleOrderConstant.LOAD_STATUS_NONE.equals(loadStatus)) {
            loadStorage = false;
        } else if (MsgCycleOrderConstant.LOAD_STATUS_HAVE.equals(loadStatus)) {
            loadStorage = true;
        } else if (MsgCycleOrderConstant.LOAD_STATUS_HAVA_CAR.equals(loadStatus)) {
            loadStorage = false;
        } else {
            loadStorage = MsgCycleOrderConstant.LOAD_STATUS_HAVE_CAR_LOAD.equals(loadStatus);
        }
        MsgCycleOrderAckService.resolveMsg(blockName, msgMcKey, ackType, exceptionType, blockMcKey, blockAppointmentMcKey, command, object, loadStorage);
    }

    @Override
    public void doMsgCycleOrderFinishReportDTO(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) throws InterruptedException {

    }

    String getBlockName() {
        return this.blockName;
    }
}
