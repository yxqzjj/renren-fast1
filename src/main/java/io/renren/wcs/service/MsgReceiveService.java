package io.renren.wcs.service;

import io.renren.wcs.client.dto.MsgDTO;

/**
 * @ClassName: MsgReceiveService
 * @Description: 消息接收处理
 * @Author: CalmLake
 * @Date: 2018/11/17  16:02
 * @Version: V1.0.0
 **/
public interface MsgReceiveService {
    /**
     * 接收字符串消息转换为对象消息
     * @author CalmLake
     * @date 2019/3/26 16:27
     * @param msg 字符串消息
     * @return com.wap.client.dto.MsgDTO
     */
    MsgDTO getMsgDTO(String msg);
}
