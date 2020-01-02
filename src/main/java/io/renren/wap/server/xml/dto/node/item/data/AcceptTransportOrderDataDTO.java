package io.renren.wap.server.xml.dto.node.item.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 数据区域信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:45
 * @Version: V1.0.0
 **/
@XStreamAlias("DataArea")
public class AcceptTransportOrderDataDTO implements Serializable {
    @XStreamAlias("StUnitId")
    private String stUnitID;
    @XStreamAlias("RouteChange")
    private String routeChange;
    @XStreamAlias("Information")
    private String information;

    public String getStUnitID() {
        return stUnitID;
    }

    public void setStUnitID(String stUnitID) {
        this.stUnitID = stUnitID;
    }

    public String getRouteChange() {
        return routeChange;
    }

    public void setRouteChange(String routeChange) {
        this.routeChange = routeChange;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
