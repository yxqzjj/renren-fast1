package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.CancelTransportOrderReportDataDTO;

import java.io.Serializable;

/**
 * @ClassName: CancelTransportOrderReportDTO
 * @Description: wcs发送的取消任务报告信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:00
 * @Version: V1.0.0
 **/
@XStreamAlias("CancelTransportOrderReport")
public class CancelTransportOrderReportDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private CancelTransportOrderReportDataDTO cancelTransportOrderReportDataDTO;

    public CancelTransportOrderReportDataDTO getCancelTransportOrderReportDataDTO() {
        return cancelTransportOrderReportDataDTO;
    }

    public void setCancelTransportOrderReportDataDTO(CancelTransportOrderReportDataDTO cancelTransportOrderReportDataDTO) {
        this.cancelTransportOrderReportDataDTO = cancelTransportOrderReportDataDTO;
    }

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }
}
