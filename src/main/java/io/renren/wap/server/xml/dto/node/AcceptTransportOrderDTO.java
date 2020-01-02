package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.AcceptTransportOrderDataDTO;

import java.io.Serializable;

/**
 * @ClassName: AcceptTransportOrderDTO
 * @Description: wcs接收任务回复信息
 * @Author: CalmLake
 * @Date: 2018/11/20  10:57
 * @Version: V1.0.0
 **/
@XStreamAlias("AcceptTransportOrder")
public class AcceptTransportOrderDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private AcceptTransportOrderDataDTO acceptTransportOrderDataDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

    public AcceptTransportOrderDataDTO getAcceptTransportOrderDataDTO() {
        return acceptTransportOrderDataDTO;
    }

    public void setAcceptTransportOrderDataDTO(AcceptTransportOrderDataDTO acceptTransportOrderDataDTO) {
        this.acceptTransportOrderDataDTO = acceptTransportOrderDataDTO;
    }
}
