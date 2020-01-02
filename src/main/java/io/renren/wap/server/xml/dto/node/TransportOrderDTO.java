package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.TransportOrderDataDTO;

import java.io.Serializable;

/**
 * wms发送的任务信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:04
 * @Version: V1.0.0
 **/
@XStreamAlias("TransportOrder")
public class TransportOrderDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private TransportOrderDataDTO transportOrderDataDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

    public TransportOrderDataDTO getTransportOrderDataDTO() {
        return transportOrderDataDTO;
    }

    public void setTransportOrderDataDTO(TransportOrderDataDTO transportOrderDataDTO) {
        this.transportOrderDataDTO = transportOrderDataDTO;
    }
}
