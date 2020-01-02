package io.renren.wap.customer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsStationmodeEntity;
import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.dto.MsgChangeStationModeDTO;
import io.renren.wap.entity.constant.StationModeConstant;
import io.renren.wap.entity.constant.WMSMessageLogConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.service.CreateXmlService;
import io.renren.wap.server.util.SendMsgToWmsUtil;
import io.renren.wap.server.xml.constant.TransportOrderConstant;
import io.renren.wap.server.xml.constant.XmlInfoConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.server.xml.dto.node.QueryTransportModeDTO;
import io.renren.wap.server.xml.dto.node.TransportModeChangeDTO;
import io.renren.wap.service.AssigningTaskService;
import io.renren.wap.service.WmsMessageLogService;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.service.msg.ClMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.logging.log4j.LogManager;

/**
 * wms消息消费者
 *
 * @Author: CalmLake
 * @Date: 2019/1/7  10:38
 * @Version: V1.0.0
 **/
public class WmsXmlQueueCustomer implements Runnable {

    /**
     * 端口号
     */
    private Integer port;

    public WmsXmlQueueCustomer(Integer port) {
        this.port = port;
    }

    @Override
    public void run() {
        boolean flag = true;
        LogManager.getLogger().info(String.format("wcs-wms消息消费者启动！端口号：%d", port));
        while (flag) {
            try {
                EnvelopeDTO envelopeDTO;
                envelopeDTO = XmlQueueCache.getReceiveMsg(port);
                WmsMessageLogService wmsMessageLogService = new WmsMessageLogService();
                WorkPlanService workPlanService = new WorkPlanService();
                if (envelopeDTO.getTransportOrderDTO() != null) {
                    //  新任务
                    String transportOrderType = envelopeDTO.getTransportOrderDTO().getTransportOrderDataDTO().getTransportType();
                    String wmsId = envelopeDTO.getTransportOrderDTO().getControlAreaDTO().getRefIdDTO().getRefId();
                    String stUnitID = envelopeDTO.getTransportOrderDTO().getTransportOrderDataDTO().getStUnitId().trim();
                    if (TransportOrderConstant.TYPE_DELETE.equals(transportOrderType)) {
                        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("wmsFlag",wmsId));
                        if (workPlan != null) {
                            DbUtil.getProcedureOrcaleDao().spDeleteFinishWorkPlanOperationIn(workPlan.getId(), workPlan.getMckey());
                        }
                        acceptTransportOrderReplay(envelopeDTO, wmsMessageLogService, wmsId, 0, stUnitID, XmlInfoConstant.XML_DEFAULT_00);
                    } else {
                        int countNum = workPlanService.selectByWmsFlag(wmsId);
                        if (countNum > 0) {
                            Log4j2Util.getXmlMsgOperationLogger().info(port + ",WMS_ID:" + wmsId + ",已存在！任务已创建！");
                            acceptTransportOrderReplay(envelopeDTO, wmsMessageLogService, wmsId, 0, stUnitID, XmlInfoConstant.XML_DEFAULT_00);
                        } else {
                            WcsWorkplanEntity workPlan = workPlanService.createWorkPlan(envelopeDTO);
                            int workPlanId = workPlan.getId();
                            if (workPlanId > 0) {
                                AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
                                assigningTaskService.assigningTasks();
                                acceptTransportOrderReplay(envelopeDTO, wmsMessageLogService, wmsId, workPlanId, stUnitID, XmlInfoConstant.XML_DEFAULT_00);
                            } else {
                                Log4j2Util.getXmlMsgOperationLogger().info(port + ",WMS_ID:" + wmsId + ",任务创建失败！");
                            }
                        }
                        WcsWmsmessagelogEntity wmsMessageLog = DbUtil.getWMSMessageLogDao().selectOne(new QueryWrapper<WcsWmsmessagelogEntity>().eq("Barcode","999999"));
                        if (wmsMessageLog != null) {
                            wmsMessageLog.setStatus(WMSMessageLogConstant.STATUS_SEND_ACK);
                            int resultInt = DbUtil.getWMSMessageLogDao().updateById(wmsMessageLog);
                            Log4j2Util.getXmlMsgOperationLogger().info(String.format("LoadUnitAtID 唯一标识：%s，托盘号：%s，接收成功,消息状态修改结果：%d", wmsMessageLog.getUuid(), stUnitID, resultInt));
                        } else {
                            Log4j2Util.getXmlMsgOperationLogger().info(String.format("数据库未找到数据，LoadUnitAtID 托盘号：%s，%s", stUnitID, envelopeDTO.toString()));
                        }
                    }
                } else if (envelopeDTO.getTransportModeChangeDTO() != null) {
                    //  站台模式切换
                    wmsMessageLogService.insertWMSMessageLog(envelopeDTO, WMSMessageLogConstant.TYPE_TRANSPORTMODECHANGE, WMSMessageLogConstant.STATUS_RECEIVED);
                    TransportModeChangeDTO transportModeChangeDTO = envelopeDTO.getTransportModeChangeDTO();
                    String station = transportModeChangeDTO.getTransportModeChangeDataDTO().getMha();
                    String workType = transportModeChangeDTO.getTransportModeChangeDataDTO().getTransportType();
                    ClMsgService clMsgService = new ClMsgService();
                    MsgChangeStationModeDTO msgChangeStationModeDTO = clMsgService.changeMode(station, workType);
                    MsgQueueCache.addSendMsg(msgChangeStationModeDTO);
                } else if (envelopeDTO.getQueryTransportModeDTO() != null) {
                    wmsMessageLogService.insertWMSMessageLog(envelopeDTO, WMSMessageLogConstant.TYPE_QUERY_TRANSPORT_MODE, WMSMessageLogConstant.STATUS_RECEIVED);
                    //  站台模式询问
                    QueryTransportModeDTO queryTransportModeDTO = envelopeDTO.getQueryTransportModeDTO();
                    String station = queryTransportModeDTO.getQueryTransportModeDataDTO().getMha();
                    WcsStationmodeEntity stationMode = DbUtil.getStationModeDao().selectOne(new QueryWrapper<WcsStationmodeEntity>().eq("Name",station));
                    String mode;
                    if (StationModeConstant.MODE_IN.equals(stationMode.getMode())) {
                        //  入库
                        mode = "01";
                    } else if (StationModeConstant.MODE_out.equals(stationMode.getMode())) {
                        //  出库
                        mode = "02";
                    } else {
                        //  错误
                        mode = "03";
                    }
                    envelopeDTO = CreateXmlService.createTransportModeChangeReport(station, mode);
                    wmsMessageLogService.insertWMSMessageLog(envelopeDTO, WMSMessageLogConstant.TYPE_TRANSPORTMODECHANGEREPORT, WMSMessageLogConstant.STATUS_RECEIVED_ACK);
                    XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, envelopeDTO);
                } else if (envelopeDTO.getQueryTaskStatusDTO() != null) {
                    //  查询任务状态（接收或者完成）
                    String wmsId = envelopeDTO.getQueryTaskStatusDTO().getControlAreaDTO().getRefIdDTO().getRefId();
                    WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("wmsFlag",wmsId));
                    if (workPlan != null) {
                        if (WorkPlanConstant.STATUS_FINISH==workPlan.getStatus()) {
                            SendMsgToWmsUtil.sendMovementReport(workPlan);
                        } else {
                            acceptTransportOrderReplay(envelopeDTO, wmsMessageLogService, wmsId, workPlan.getId(), workPlan.getBarcode(), XmlInfoConstant.XML_DEFAULT_00);
                        }
                    } else {
                        WcsWorkplanlogEntity workPlanLog = DbUtil.getWorkPlanLogDao().selectById(wmsId);
                        if (workPlanLog != null) {
                            if (WorkPlanConstant.STATUS_FINISH==workPlanLog.getStatus()) {
                                SendMsgToWmsUtil.sendMovementReport(workPlanLog);
                            } else {
                                acceptTransportOrderReplay(envelopeDTO, wmsMessageLogService, wmsId, workPlanLog.getWorkPlanId(), workPlanLog.getBarcode(), XmlInfoConstant.XML_DEFAULT_00);
                            }
                        } else {
                            Log4j2Util.getXmlMsgOperationLogger().info(String.format("QueryTaskStatus 唯一标识：%s，没有该标识信息", wmsId));
                        }
                    }
                } else if (envelopeDTO.getLoadUnitAtIDDTO() != null) {
                    //  LoadUnitAtID wms->wcs 请求任务应答
                    String uuid = envelopeDTO.getLoadUnitAtIDDTO().getControlAreaDTO().getRefIdDTO().getRefId();
                    String barCode = envelopeDTO.getLoadUnitAtIDDTO().getLoadUnitAtIDDataDTO().getScanDate().trim();
                    WcsWmsmessagelogEntity wmsMessageLog = DbUtil.getWMSMessageLogDao().selectOne(new QueryWrapper<WcsWmsmessagelogEntity>().eq("UUID",uuid));
                    if (wmsMessageLog != null) {
                        wmsMessageLog.setStatus(WMSMessageLogConstant.STATUS_SEND_ACK);
                        int resultInt = DbUtil.getWMSMessageLogDao().update(wmsMessageLog,new QueryWrapper<WcsWmsmessagelogEntity>().eq("UUID",uuid));
                        Log4j2Util.getXmlMsgOperationLogger().info(String.format("LoadUnitAtID 唯一标识：%s，托盘号：%s，接收成功,消息状态修改结果：%d", uuid, barCode, resultInt));
                    } else {
                        Log4j2Util.getXmlMsgOperationLogger().info(String.format("LoadUnitAtID 唯一标识：%s，%s", uuid, envelopeDTO.toString()));
                    }
                } else {
                    Log4j2Util.getXmlMsgOperationLogger().info(port + ",未解析的消息类型" + envelopeDTO.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
                Log4j2Util.getXmlMsgOperationLogger().info(port + ",消息队列终止运行", e);
            }
        }
        Log4j2Util.getXmlMsgOperationLogger().info(port + ",消费者退出！");
    }

    /**
     * 接受任务后回复wms消息，回复输送线55
     *
     * @param envelopeDTO          xml消息对象
     * @param wmsMessageLogService db表操作类
     * @param wmsId                wms任务唯一标识
     * @param workPlanId           工作计划id
     * @author CalmLake
     * @date 2019/4/1 10:52
     */
    private void acceptTransportOrderReplay(EnvelopeDTO envelopeDTO, WmsMessageLogService wmsMessageLogService, String wmsId, int workPlanId, String stUnitID, String routeChange) throws InterruptedException {
        EnvelopeDTO envelopeDTOAccept = CreateXmlService.createAcceptTransportOrder(wmsId, stUnitID, routeChange, XmlInfoConstant.XML_DEFAULT_00);
        XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, port, envelopeDTOAccept);
        wmsMessageLogService.insertWMSMessageLog(wmsId, stUnitID, workPlanId, envelopeDTO, WMSMessageLogConstant.TYPE_TRANSPORTORDER, WMSMessageLogConstant.STATUS_RECEIVED, "");
        wmsMessageLogService.insertWMSMessageLog(wmsId, stUnitID, workPlanId, envelopeDTOAccept, WMSMessageLogConstant.TYPE_ACCEPTTRANSPORTORDER, WMSMessageLogConstant.STATUS_RECEIVED_ACK, "");
    }
}
