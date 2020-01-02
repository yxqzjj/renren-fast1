package io.renren.wap.thread.callable;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.constant.CallableConstant;
import io.renren.wap.entity.constant.WMSMessageLogConstant;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

import java.util.concurrent.Callable;

/**
 * LoadUnitAtID 任务创建请求消息重发
 *
 * @Author: CalmLake
 * @Date: 2019/5/19  11:08
 * @Version: V1.0.0
 **/
public class ResendLoadUnitAtIdCallable implements Callable<String> {
    private EnvelopeDTO envelopeDTO;

    public ResendLoadUnitAtIdCallable(EnvelopeDTO envelopeDTO) {
        this.envelopeDTO = envelopeDTO;
    }

    @Override
    public String call() throws Exception {
        long time = System.currentTimeMillis() / 1000;
        int resendNum = 1;
        while (true) {
            if (resendNum > SystemCache.RESEND_LOADUNITATID_MAX) {
                //  超过最大次数退出
                Log4j2Util.getXmlMsgOperationLogger().info(String.format("ResendLoadUnitAtIdCallable envelopeDTO: %s 发送超过最大次数", envelopeDTO.toString()));
                return CallableConstant.ERROR;
            }
            long nowTime = System.currentTimeMillis() / 1000;
            if ((nowTime - time) > SystemCache.RESEND_LOADUNITATID_TIME_INTERVAL) {
                String uuid = envelopeDTO.getLoadUnitAtIDDTO().getControlAreaDTO().getRefIdDTO().getRefId();
                WcsWmsmessagelogEntity wmsMessageLog = DbUtil.getWMSMessageLogDao().selectOne(new QueryWrapper<WcsWmsmessagelogEntity>().eq("UUID",uuid));
                if (wmsMessageLog != null) {
                    if (WMSMessageLogConstant.STATUS_SEND.equals(wmsMessageLog.getStatus())) {
                        XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, envelopeDTO);
                        resendNum++;
                        time = System.currentTimeMillis() / 1000;
                    } else if (WMSMessageLogConstant.STATUS_SEND_ACK.equals(wmsMessageLog.getStatus())) {
                        return CallableConstant.RECEIVE_SUCCESS;
                    } else {
                        Log4j2Util.getXmlMsgOperationLogger().info(String.format("ResendLoadUnitAtIdCallable uuid: %s 状态判断无效", uuid));
                    }
                }
            }
            Thread.sleep(CallableConstant.WAIT);
        }
    }
}
