package io.renren.wcs.client.Factory;

import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.service.MsgReceiveService;
import io.renren.wcs.service.MsgSendService;

/**
 * 消息操作抽象工厂
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  17:26
 * @Version: V1.0.0
 **/
public abstract class AbstractFactory {
    /**
     * 获得操作消息服务类
     *
     * @param msgDTO 消息对象
     * @author CalmLake
     * @date 2019/3/26 16:33
     */
    public abstract MsgSendService getMsgSendService(MsgDTO msgDTO);

    /**
     * 获得操作消息服务类
     *
     * @param msg 消息字符串
     * @return com.wap.client.service.MsgReceiveService
     * @author CalmLake
     * @date 2019/3/26 16:33
     */
    public abstract MsgReceiveService getMsgReceiveService(String msg);
}
