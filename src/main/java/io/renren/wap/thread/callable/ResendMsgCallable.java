package io.renren.wap.thread.callable;


import io.renren.modules.generator.entity.Block;
import io.renren.wap.cache.BlockCache;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgCycleOrderDTO;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.constant.CallableConstant;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.factory.BlockFactory;
import io.renren.wap.factory.BlockImpl;
import io.renren.wap.util.SleepUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;

/**
 * 消息重发
 *
 * @Author: CalmLake
 * @Date: 2019/1/14  10:20
 * @Version: V1.0.0
 **/
public class ResendMsgCallable implements Callable<String> {
    private String blockName;
    private MsgDTO msgDTO;

    public ResendMsgCallable(String blockName, MsgDTO msgDTO) {
        this.blockName = blockName;
        this.msgDTO = msgDTO;
    }

    @Override
    public String call() throws Exception {
        String command;
        String errorCode;
        String mcKey;
        String appointmentMcKey;
        String status;
        int resendNum = 1;
        BlockFactory blockFactory = new BlockImpl();
        while (true) {
            SleepUtil.sleep(SystemCache.RESEND_TIME_INTERVAL);
            String cycleCommand = ((MsgCycleOrderDTO) msgDTO).getCycleCommand();
            Block block = blockFactory.getBlock(MachineCache.getMachine(blockName));
            command = block.getCommand();
            errorCode = block.getErrorCode();
            mcKey = block.getMckey();
            appointmentMcKey = block.getAppointmentMckey();
            status = block.getStatus();
            if (!MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER.equals(command)) {
                //  ！= 03 成功 退出
                return CallableConstant.RECEIVE_SUCCESS;
            }
            if (BlockConstant.STATUS_BAN.equals(status)){
                //  设备禁止
                return CallableConstant.ERROR;
            }
            if (!BlockConstant.ERROR_CODE_DEFAULT.equals(errorCode)) {
                //  出错退出
                return CallableConstant.ERROR;
            }
            if (StringUtils.isEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
                //  任务标识清除退出
                return CallableConstant.ERROR;
            }
            if (!cycleCommand.equals(BlockCache.getString(blockName))) {
                //  发送消息动作与缓存执行动作不一致退出
                return CallableConstant.ERROR;
            }
            if (resendNum > SystemCache.RESEND_MMAX) {
                //  超过最大次数退出
                return CallableConstant.ERROR;
            }
            //  重发
            ++resendNum;
            MsgQueueCache.addSendMsg(msgDTO);
        }
    }
}
