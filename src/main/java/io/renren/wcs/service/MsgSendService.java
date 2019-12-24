package io.renren.wcs.service;

import io.renren.wcs.client.dto.MsgDTO;

/**
 * @ClassName: BlockMsgSendService
 * @Description: 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  16:00
 * @Version: V1.0.0
 **/
public interface MsgSendService {
    /**
     * 将消息对象转换为字节数组
     * @author CalmLake
     * @date 2019/3/26 16:29
     * @param msgDTO 发送消息对象
     * @return byte[]
     */
    byte[] msgDTOToBytes(MsgDTO msgDTO);
}
