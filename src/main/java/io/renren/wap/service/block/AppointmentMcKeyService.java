package io.renren.wap.service.block;

import io.renren.wap.client.dto.MsgDTO;

/**
 * 预约任务处理接口
 *
 * @Author: CalmLake
 * @Date: 2019/3/27  15:57
 * @Version: V1.0.0
 **/
public interface AppointmentMcKeyService {
    /**
     * 预约任务处理
     *
     * @param object 数据block对象
     * @return MsgDTO 制作消息对象
     * @throws InterruptedException 异常
     * @author CalmLake
     * @date 2019/3/27 16:08
     */
    MsgDTO appointmentMcKey(Object object) throws InterruptedException;
}
