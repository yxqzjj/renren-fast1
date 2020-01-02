package io.renren.wap.server.xml.dto.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.data.QueryTransportModeDataDTO;

import java.io.Serializable;

/**
 * wms发送的站台模式查询信息
 *
 * @Author: CalmLake
 * @Date: 2018/11/20  11:05
 * @Version: V1.0.0
 **/
@XStreamAlias("QueryTransportMode")
public class QueryTransportModeDTO implements Serializable {
    @XStreamAlias("ControlArea")
    private ControlAreaDTO controlAreaDTO;
    @XStreamAlias("DataArea")
    private QueryTransportModeDataDTO queryTransportModeDataDTO;

    public ControlAreaDTO getControlAreaDTO() {
        return controlAreaDTO;
    }

    public void setControlAreaDTO(ControlAreaDTO controlAreaDTO) {
        this.controlAreaDTO = controlAreaDTO;
    }

    public QueryTransportModeDataDTO getQueryTransportModeDataDTO() {
        return queryTransportModeDataDTO;
    }

    public void setQueryTransportModeDataDTO(QueryTransportModeDataDTO queryTransportModeDataDTO) {
        this.queryTransportModeDataDTO = queryTransportModeDataDTO;
    }
}
