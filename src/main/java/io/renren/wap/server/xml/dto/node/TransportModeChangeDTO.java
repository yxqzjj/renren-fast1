package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.TransportModeChangeDataDTO;

import java.io.Serializable;

/**
 * wms发送的站台切换信息
 * @Author: CalmLake
 * @Date: 2018/11/20  10:53
 * @Version: V1.0.0
 **/
@XStreamAlias("TransportModeChange")
public class TransportModeChangeDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private TransportModeChangeDataDTO transportModeChangeDataDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

    public TransportModeChangeDataDTO getTransportModeChangeDataDTO() {
        return transportModeChangeDataDTO;
    }

    public void setTransportModeChangeDataDTO(TransportModeChangeDataDTO transportModeChangeDataDTO) {
        this.transportModeChangeDataDTO = transportModeChangeDataDTO;
    }
}
