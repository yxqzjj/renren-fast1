package io.renren.wap.server.xml.dto.node.item.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 数据区域信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:47
 * @Version: V1.0.0
 **/
@XStreamAlias("DataArea")
public class CancelTransportOrderDataDTO implements Serializable {
    @XStreamAlias("StUnit")
    private String stUnit;

    public String getStUnit() {
        return stUnit;
    }

    public void setStUnit(String stUnit) {
        this.stUnit = stUnit;
    }
}
