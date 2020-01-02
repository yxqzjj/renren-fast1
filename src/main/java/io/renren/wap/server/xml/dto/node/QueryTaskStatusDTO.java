package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;

import java.io.Serializable;

/**
 * @ClassName: QueryTaskStatusDTO
 * @Description: wms发送的任务状态查询信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:06
 * @Version: V1.0.0
 **/
@XStreamAlias("QueryTaskStatus")
public class QueryTaskStatusDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

}
