package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.CancelTransportOrderDataDTO;

import java.io.Serializable;

/**
 * @ClassName: CancelTransportOrderDTO
 * @Description: wms发送的取消任务信息
 * @Author: CalmLake
 * @Date: 2018/11/20  10:59
 * @Version: V1.0.0
 **/
@XStreamAlias("CancelTransportOrder")
public class CancelTransportOrderDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private CancelTransportOrderDataDTO cancelTransportOrderDataDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

    public CancelTransportOrderDataDTO getCancelTransportOrderDataDTO() {
        return cancelTransportOrderDataDTO;
    }

    public void setCancelTransportOrderDataDTO(CancelTransportOrderDataDTO cancelTransportOrderDataDTO) {
        this.cancelTransportOrderDataDTO = cancelTransportOrderDataDTO;
    }
}
