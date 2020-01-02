package io.renren.wap.server.util;


import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import io.renren.wap.entity.constant.WMSMessageLogConstant;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.service.CreateXmlService;
import io.renren.wap.server.xml.constant.XmlInfoConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.service.WmsMessageLogService;

/**
 * 发送消息至wms
 *
 * @Author: CalmLake
 * @Date: 2019/3/29  16:18
 * @Version: V1.0.0
 **/
public class SendMsgToWmsUtil {

    /**
     * 向wms发送任务完成报告
     *
     * @param workPlan 工作计划信息
     * @author CalmLake
     * @date 2019/3/29 16:21
     */
    public static void sendMovementReport(WcsWorkplanEntity workPlan) throws InterruptedException {
        String stUniId = workPlan.getBarcode();
        String wmsId = workPlan.getWmsFlag();
        String fromStation = workPlan.getFromStation();
        String toStation = workPlan.getToStation();
        String fromLocation = workPlan.getFromLocation();
        String toLocation = workPlan.getToLocation();
        EnvelopeDTO envelopeDTO = CreateXmlService.createMovementReport(stUniId, wmsId, fromStation, fromLocation, toStation, toLocation, XmlInfoConstant.XML_DEFAULT_00, XmlInfoConstant.XML_DEFAULT_00);
        XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, envelopeDTO);
        WmsMessageLogService wmsMessageLogService=new WmsMessageLogService();
        wmsMessageLogService.insertWMSMessageLog(wmsId,stUniId,workPlan.getId(),envelopeDTO, WMSMessageLogConstant.TYPE_MOVEMENTREPORT,WMSMessageLogConstant.STATUS_SEND,"");
    }

    /**
     * 向wms发送任务完成报告
     *
     * @param workPlanLog 工作计划信息记录
     * @author CalmLake
     * @date 2019/3/29 16:21
     */
    public static void sendMovementReport(WcsWorkplanlogEntity workPlanLog) throws InterruptedException {
        String stUniId = workPlanLog.getBarcode();
        String wmsId = workPlanLog.getWmsFlag();
        String fromStation = workPlanLog.getFromStation();
        String toStation = workPlanLog.getToStation();
        String fromLocation = workPlanLog.getFromLocation();
        String toLocation = workPlanLog.getToLocation();
        EnvelopeDTO envelopeDTO = CreateXmlService.createMovementReport(stUniId, wmsId, fromStation, fromLocation, toStation, toLocation, XmlInfoConstant.XML_DEFAULT_00, XmlInfoConstant.XML_DEFAULT_00);
        XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, envelopeDTO);
        WmsMessageLogService wmsMessageLogService=new WmsMessageLogService();
        wmsMessageLogService.insertWMSMessageLog(wmsId,stUniId,workPlanLog.getWorkPlanId(),envelopeDTO, WMSMessageLogConstant.TYPE_MOVEMENTREPORT,WMSMessageLogConstant.STATUS_SEND,"");
    }
}
