package io.renren.wap.server.xml.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.*;

import java.io.Serializable;

/**
 * wms和wcs通讯 xml根目录
 * @Author: CalmLake
 * @Date: 2018/11/20  10:46
 * @Version: V1.0.0
 **/
@XStreamAlias("WmsWcsXML_Envelope")
public class EnvelopeDTO implements Serializable {
    @XStreamAlias("TransportModeChange")
    private TransportModeChangeDTO transportModeChangeDTO;
    @XStreamAlias("TransportModeChangeReport")
    private TransportModeChangeReportDTO transportModeChangeReportDTO;
    @XStreamAlias("AcceptTransportOrder")
    private AcceptTransportOrderDTO acceptTransportOrderDTO;
    @XStreamAlias("CancelTransportOrder")
    private CancelTransportOrderDTO cancelTransportOrderDTO;
    @XStreamAlias("CancelTransportOrderReport")
    private CancelTransportOrderReportDTO cancelTransportOrderReportDTO;
    @XStreamAlias("LoadUnitAtID")
    private LoadUnitAtIDDTO loadUnitAtIDDTO;
    @XStreamAlias("MovementReport")
    private MovementReportDTO movementReportDTO;
    @XStreamAlias("TransportOrder")
    private TransportOrderDTO transportOrderDTO;
    @XStreamAlias("QueryTransportMode")
    private QueryTransportModeDTO queryTransportModeDTO;
    @XStreamAlias("QueryTaskStatus")
    private QueryTaskStatusDTO queryTaskStatusDTO;

    public TransportModeChangeDTO getTransportModeChangeDTO() {
        return transportModeChangeDTO;
    }

    public void setTransportModeChangeDTO(TransportModeChangeDTO transportModeChangeDTO) {
        this.transportModeChangeDTO = transportModeChangeDTO;
    }

    public TransportModeChangeReportDTO getTransportModeChangeReportDTO() {
        return transportModeChangeReportDTO;
    }

    public void setTransportModeChangeReportDTO(TransportModeChangeReportDTO transportModeChangeReportDTO) {
        this.transportModeChangeReportDTO = transportModeChangeReportDTO;
    }

    public AcceptTransportOrderDTO getAcceptTransportOrderDTO() {
        return acceptTransportOrderDTO;
    }

    public void setAcceptTransportOrderDTO(AcceptTransportOrderDTO acceptTransportOrderDTO) {
        this.acceptTransportOrderDTO = acceptTransportOrderDTO;
    }

    public CancelTransportOrderDTO getCancelTransportOrderDTO() {
        return cancelTransportOrderDTO;
    }

    public void setCancelTransportOrderDTO(CancelTransportOrderDTO cancelTransportOrderDTO) {
        this.cancelTransportOrderDTO = cancelTransportOrderDTO;
    }

    public CancelTransportOrderReportDTO getCancelTransportOrderReportDTO() {
        return cancelTransportOrderReportDTO;
    }

    public void setCancelTransportOrderReportDTO(CancelTransportOrderReportDTO cancelTransportOrderReportDTO) {
        this.cancelTransportOrderReportDTO = cancelTransportOrderReportDTO;
    }

    public LoadUnitAtIDDTO getLoadUnitAtIDDTO() {
        return loadUnitAtIDDTO;
    }

    public void setLoadUnitAtIDDTO(LoadUnitAtIDDTO loadUnitAtIDDTO) {
        this.loadUnitAtIDDTO = loadUnitAtIDDTO;
    }

    public MovementReportDTO getMovementReportDTO() {
        return movementReportDTO;
    }

    public void setMovementReportDTO(MovementReportDTO movementReportDTO) {
        this.movementReportDTO = movementReportDTO;
    }

    public TransportOrderDTO getTransportOrderDTO() {
        return transportOrderDTO;
    }

    public void setTransportOrderDTO(TransportOrderDTO transportOrderDTO) {
        this.transportOrderDTO = transportOrderDTO;
    }

    public QueryTransportModeDTO getQueryTransportModeDTO() {
        return queryTransportModeDTO;
    }

    public void setQueryTransportModeDTO(QueryTransportModeDTO queryTransportModeDTO) {
        this.queryTransportModeDTO = queryTransportModeDTO;
    }

    public QueryTaskStatusDTO getQueryTaskStatusDTO() {
        return queryTaskStatusDTO;
    }

    public void setQueryTaskStatusDTO(QueryTaskStatusDTO queryTaskStatusDTO) {
        this.queryTaskStatusDTO = queryTaskStatusDTO;
    }
}
