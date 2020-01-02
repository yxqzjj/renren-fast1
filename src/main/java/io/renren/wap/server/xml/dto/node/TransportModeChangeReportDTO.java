package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.TransportModeChangeReportDataDTO;

import java.io.Serializable;

/**
 * @ClassName: TransportModeChangeReportDTO
 * @Description: wcs回复的站台切换结果报告信息
 * @Author: CalmLake
 * @Date: 2018/11/20  10:54
 * @Version: V1.0.0
 **/
@XStreamAlias("TransportModeChangeReport")
public class TransportModeChangeReportDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private TransportModeChangeReportDataDTO transportModeChangeReportDataDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

    public TransportModeChangeReportDataDTO getTransportModeChangeReportDataDTO() {
        return transportModeChangeReportDataDTO;
    }

    public void setTransportModeChangeReportDataDTO(TransportModeChangeReportDataDTO transportModeChangeReportDataDTO) {
        this.transportModeChangeReportDataDTO = transportModeChangeReportDataDTO;
    }
}
