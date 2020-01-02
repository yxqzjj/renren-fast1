package io.renren.wap.server.service;


import io.renren.wap.constant.DatePatternConstant;
import io.renren.wap.server.xml.constant.XmlInfoConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.server.xml.dto.node.AcceptTransportOrderDTO;
import io.renren.wap.server.xml.dto.node.MovementReportDTO;
import io.renren.wap.server.xml.dto.node.TransportModeChangeReportDTO;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.control.ReceiverDTO;
import io.renren.wap.server.xml.dto.node.item.control.RefIdDTO;
import io.renren.wap.server.xml.dto.node.item.control.SenderDTO;
import io.renren.wap.server.xml.dto.node.item.data.AcceptTransportOrderDataDTO;
import io.renren.wap.server.xml.dto.node.item.data.MovementReportDataDTO;
import io.renren.wap.server.xml.dto.node.item.data.TransportModeChangeReportDataDTO;
import io.renren.wap.server.xml.dto.node.item.data.location.LocationDetailDTO;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 制作xml消息
 *
 * @Author: CalmLake
 * @Date: 2019/1/8  11:51
 * @Version: V1.0.0
 **/
public class CreateXmlService {

    /**
     * 创建任务完成报告
     *
     * @param stUniId      托盘号
     * @param wmsId        wms任务唯一标识
     * @param mhaFrom      源站台名称
     * @param rackListFrom 源排列层
     * @param mhaTo        目标站台名称
     * @param rackListTo   目标排列层
     * @param information  信息（根据现场实际情况自定义）
     * @param reasonCode   错误原因（根据现场实际情况自定义）
     * @return com.wap.server.xml.dto.EnvelopeDTO
     * @author CalmLake
     * @date 2019/1/22 10:41
     */
    public static EnvelopeDTO createMovementReport(String stUniId, String wmsId, String mhaFrom, String rackListFrom, String mhaTo, String rackListTo, String information, String reasonCode) {
        List<String> stringFromList = new ArrayList<>();
        List<String> stringToList = new ArrayList<>();
        WorkPlanService.getListRackString(rackListFrom, stringFromList);
        WorkPlanService.getListRackString(rackListTo, stringToList);
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        MovementReportDTO movementReportDTO = new MovementReportDTO();
        ControlAreaDTO controlAreaDTO = new ControlAreaDTO();
        MovementReportDataDTO movementReportDataDTO = new MovementReportDataDTO();
        SenderDTO senderDTO = new SenderDTO();
        ReceiverDTO receiverDTO = new ReceiverDTO();
        RefIdDTO refIdDTO = new RefIdDTO();
        LocationDetailDTO locationDetailFromDTO = new LocationDetailDTO();
        LocationDetailDTO locationDetailToDTO = new LocationDetailDTO();
        senderDTO.setDivision(XmlInfoConstant.XML_WCS_NAME);
        receiverDTO.setDivision(XmlInfoConstant.XML_WMS_NAME);
        refIdDTO.setRefId(wmsId);
        locationDetailFromDTO.setMha(mhaFrom);
        locationDetailFromDTO.setRack(stringFromList);
        locationDetailToDTO.setMha(mhaTo);
        locationDetailToDTO.setRack(stringToList);
        controlAreaDTO.setRefIdDTO(refIdDTO);
        controlAreaDTO.setSenderDTO(senderDTO);
        controlAreaDTO.setReceiverDTO(receiverDTO);
        controlAreaDTO.setCreationDateTime(DateFormatUtil.dateToString(new Date(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
        movementReportDataDTO.setFromLocation(locationDetailFromDTO);
        movementReportDataDTO.setToLocation(locationDetailToDTO);
        movementReportDataDTO.setInformation(information);
        movementReportDataDTO.setReasonCode(reasonCode);
        movementReportDataDTO.setStUnitId(stUniId);
        movementReportDTO.setControlAreaDTO(controlAreaDTO);
        movementReportDTO.setMovementReportDataDTO(movementReportDataDTO);
        envelopeDTO.setMovementReportDTO(movementReportDTO);
        return envelopeDTO;
    }

    /**
     * 创建 接收任务消息
     *
     * @param id          唯一标识
     * @param stUnitID    暂放托盘号
     * @param routeChange 路径信息
     * @param information 暂无
     * @return com.wap.server.xml.dto.EnvelopeDTO
     * @author CalmLake
     * @date 2019/1/8 12:04
     */
    public static EnvelopeDTO createAcceptTransportOrder(String id, String stUnitID, String routeChange, String information) {
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        RefIdDTO refIdDTO = new RefIdDTO();
        refIdDTO.setRefId(id);
        ReceiverDTO receiverDTO = new ReceiverDTO();
        receiverDTO.setDivision(XmlInfoConstant.XML_WMS_NAME);
        SenderDTO senderDTO = new SenderDTO();
        senderDTO.setDivision(XmlInfoConstant.XML_WCS_NAME);
        ControlAreaDTO controlAreaDTO = new ControlAreaDTO();
        controlAreaDTO.setCreationDateTime(DateFormatUtil.dateToString(new Date(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
        controlAreaDTO.setReceiverDTO(receiverDTO);
        controlAreaDTO.setSenderDTO(senderDTO);
        controlAreaDTO.setRefIdDTO(refIdDTO);
        AcceptTransportOrderDataDTO acceptTransportOrderDataDTO = new AcceptTransportOrderDataDTO();
        acceptTransportOrderDataDTO.setInformation(information);
        acceptTransportOrderDataDTO.setRouteChange(routeChange);
        acceptTransportOrderDataDTO.setStUnitID(stUnitID);
        AcceptTransportOrderDTO acceptTransportOrderDTO = new AcceptTransportOrderDTO();
        acceptTransportOrderDTO.setAcceptTransportOrderDataDTO(acceptTransportOrderDataDTO);
        acceptTransportOrderDTO.setControlAreaDTO(controlAreaDTO);
        envelopeDTO.setAcceptTransportOrderDTO(acceptTransportOrderDTO);
        return envelopeDTO;
    }

    /**
     * 创建 站台模式切换报告消息
     *
     * @param station 站台名称
     * @param mode    模式
     * @return com.wap.server.xml.dto.EnvelopeDTO
     * @author CalmLake
     * @date 2019/1/21 16:46
     */
    public static EnvelopeDTO createTransportModeChangeReport(String station, String mode) {
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        TransportModeChangeReportDTO transportModeChangeReportDTO = new TransportModeChangeReportDTO();
        ControlAreaDTO controlAreaDTO = new ControlAreaDTO();
        TransportModeChangeReportDataDTO transportModeChangeReportDataDTO = new TransportModeChangeReportDataDTO();
        SenderDTO senderDTO = new SenderDTO();
        ReceiverDTO receiverDTO = new ReceiverDTO();
        senderDTO.setDivision(XmlInfoConstant.XML_WCS_NAME);
        receiverDTO.setDivision(XmlInfoConstant.XML_WMS_NAME);
        controlAreaDTO.setSenderDTO(senderDTO);
        controlAreaDTO.setReceiverDTO(receiverDTO);
        controlAreaDTO.setCreationDateTime(DateFormatUtil.dateToString(new Date(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
        transportModeChangeReportDataDTO.setInformation(XmlInfoConstant.XML_DEFAULT_00);
        transportModeChangeReportDataDTO.setMha(station);
        transportModeChangeReportDataDTO.setTransportType(mode);
        transportModeChangeReportDTO.setControlAreaDTO(controlAreaDTO);
        transportModeChangeReportDTO.setTransportModeChangeReportDataDTO(transportModeChangeReportDataDTO);
        envelopeDTO.setTransportModeChangeReportDTO(transportModeChangeReportDTO);
        return envelopeDTO;
    }

}
