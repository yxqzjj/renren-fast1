package io.renren.wap.server.xml.dto.node.item.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.data.location.LocationDetailDTO;

import java.io.Serializable;

/**
 * 数据区域信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:52
 * @Version: V1.0.0
 **/
@XStreamAlias("DataArea")
public class TransportOrderDataDTO implements Serializable {
    @XStreamAlias("StUnitId")
    private String stUnitId;
    @XStreamAlias("RouteChange")
    private String routeChange;
    @XStreamAlias("TransportType")
    private String transportType;
    @XStreamAlias("FromLocation")
    private LocationDetailDTO fromLocation;
    @XStreamAlias("ToLocation")
    private LocationDetailDTO toLocation;
    @XStreamAlias("ErrorCode")
    private String errorCode;
    @XStreamAlias("Information")
    private String information;
    @XStreamAlias("Weight")
    private String weight;
    @XStreamAlias("LoadType")
    private String loadType;

    public String getStUnitId() {
        return stUnitId;
    }

    public void setStUnitId(String stUnitId) {
        this.stUnitId = stUnitId;
    }

    public String getRouteChange() {
        return routeChange;
    }

    public void setRouteChange(String routeChange) {
        this.routeChange = routeChange;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public LocationDetailDTO getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(LocationDetailDTO fromLocation) {
        this.fromLocation = fromLocation;
    }

    public LocationDetailDTO getToLocation() {
        return toLocation;
    }

    public void setToLocation(LocationDetailDTO toLocation) {
        this.toLocation = toLocation;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }
}
