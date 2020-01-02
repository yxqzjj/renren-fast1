package io.renren.wap.server.xml.dto.node.item.control;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * @ClassName: RefIdDTO
 * @Description: xml通讯唯一标识信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:37
 * @Version: V1.0.0
 **/
@XStreamAlias("RefId")
public class RefIdDTO implements Serializable {
    @XStreamAlias("Id")
    private String refId;

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }
}
