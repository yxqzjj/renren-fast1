package io.renren.wap.server.xml.dto.node.item.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.data.location.LocationDTO;

import java.io.Serializable;

/**
 * 数据区域信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:48
 * @Version: V1.0.0
 **/
@XStreamAlias("DataArea")
public class CancelTransportOrderReportDataDTO implements Serializable {
    @XStreamAlias("Location")
    private LocationDTO locationDTO;
    @XStreamAlias("StUnitID")
    private String stUnitID;
    @XStreamAlias("Information")
    private String information;

    public LocationDTO getLocationDTO() {
        return locationDTO;
    }

    public void setLocationDTO(LocationDTO locationDTO) {
        this.locationDTO = locationDTO;
    }

    public String getStUnitID() {
        return stUnitID;
    }

    public void setStUnitID(String stUnitID) {
        this.stUnitID = stUnitID;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
