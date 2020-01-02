package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.MovementReportDataDTO;

import java.io.Serializable;

/**
 * @ClassName: MovementReportDTO
 * @Description: wcs发送的任务完成报告信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:03
 * @Version: V1.0.0
 **/
@XStreamAlias("MovementReport")
public class MovementReportDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private MovementReportDataDTO movementReportDataDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

    public MovementReportDataDTO getMovementReportDataDTO() {
        return movementReportDataDTO;
    }

    public void setMovementReportDataDTO(MovementReportDataDTO movementReportDataDTO) {
        this.movementReportDataDTO = movementReportDataDTO;
    }
}
