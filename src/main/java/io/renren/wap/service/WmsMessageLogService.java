package io.renren.wap.service;


import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.server.xml.util.XStreamUtil;
import io.renren.wap.util.DbUtil;

import java.util.Date;

/**
 * wms消息记录服务层
 *
 * @Author: CalmLake
 * @Date: 2019/1/8  13:23
 * @Version: V1.0.0
 **/
public class WmsMessageLogService {

    /**
     * 动态sql插入 且返回主键id至对象
     *
     * @param id wms任务唯一标识
     * @param stUnitID 托盘号
     * @param workPlanId 工作计划id
     * @param envelopeDTO 消息内容
     * @param type 消息类型
     * @param uuid uuid
     * @author CalmLake
     * @date 2019/1/8 13:28
     */
    public void insertWMSMessageLog(String id, String stUnitID, int workPlanId, EnvelopeDTO envelopeDTO, String type, Integer status, String uuid) {
        WcsWmsmessagelogEntity wmsMessageLogInsert = new WcsWmsmessagelogEntity();
        wmsMessageLogInsert.setBarcode(stUnitID);
        wmsMessageLogInsert.setCreateTime(new Date());
        wmsMessageLogInsert.setWmsId(id);
        wmsMessageLogInsert.setUuid(uuid);
        wmsMessageLogInsert.setWorkPlanId(workPlanId);
        wmsMessageLogInsert.setMessage(XStreamUtil.toXMLString(envelopeDTO));
        wmsMessageLogInsert.setType(type);
        wmsMessageLogInsert.setStatus(status);
        DbUtil.getWMSMessageLogDao().insert(wmsMessageLogInsert);
    }

    /**
     * 动态sql插入 且返回主键id至对象
     *
     * @param envelopeDTO 消息
     * @param type        消息类型
     * @param status      消息状态
     * @return int
     * @author CalmLake
     * @date 2019/4/1 15:35
     */
    public int insertWMSMessageLog(EnvelopeDTO envelopeDTO, String type, Integer status) {
        WcsWmsmessagelogEntity wmsMessageLogInsert = new WcsWmsmessagelogEntity();
        wmsMessageLogInsert.setCreateTime(new Date());
        wmsMessageLogInsert.setMessage(XStreamUtil.toXMLString(envelopeDTO));
        wmsMessageLogInsert.setType(type);
        wmsMessageLogInsert.setStatus(status);
        return DbUtil.getWMSMessageLogDao().insertProvider(wmsMessageLogInsert);
    }

    /**
     * 根据wmsId查找
     *
     * @param wmsId wms任务唯一标识
     * @return com.wap.entity.WmsMessageLog
     * @author CalmLake
     * @date 2019/1/8 13:26
     */
    public WcsWmsmessagelogEntity selectByWmsId(String wmsId) {
        return DbUtil.getWMSMessageLogDao().selectById(wmsId);
    }
}
