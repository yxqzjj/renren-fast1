package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.LoadUnitAtIDDataDTO;

import java.io.Serializable;

/**
 * @ClassName: LoadUnitAtIDDTO
 * @Description: wcs发送的站台货物信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:01
 * @Version: V1.0.0
 **/
@XStreamAlias("LoadUnitAtID")
public class LoadUnitAtIDDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private LoadUnitAtIDDataDTO loadUnitAtIDDataDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

    public LoadUnitAtIDDataDTO getLoadUnitAtIDDataDTO() {
        return loadUnitAtIDDataDTO;
    }

    public void setLoadUnitAtIDDataDTO(LoadUnitAtIDDataDTO loadUnitAtIDDataDTO) {
        this.loadUnitAtIDDataDTO = loadUnitAtIDDataDTO;
    }
}
