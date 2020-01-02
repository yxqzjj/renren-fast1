package io.renren.wap.server.xml.dto.node.item.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.data.location.LocationDetailDTO;

import java.io.Serializable;

/**
 * 数据区域信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:50
 * @Version: V1.0.0
 **/
@XStreamAlias("DataArea")
public class MovementReportDataDTO implements Serializable {
    @XStreamAlias("FromLocation")
    private LocationDetailDTO fromLocation;
    @XStreamAlias("StUnitId")
    private String stUnitId;
    @XStreamAlias("ToLocation")
    private LocationDetailDTO toLocation;
    @XStreamAlias("ReasonCode")
    private String reasonCode;
    @XStreamAlias("Information")
    private String information;

    public LocationDetailDTO getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(LocationDetailDTO fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getStUnitId() {
        return stUnitId;
    }

    public void setStUnitId(String stUnitId) {
        this.stUnitId = stUnitId;
    }

    public LocationDetailDTO getToLocation() {
        return toLocation;
    }

    public void setToLocation(LocationDetailDTO toLocation) {
        this.toLocation = toLocation;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
