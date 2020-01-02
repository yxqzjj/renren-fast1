package io.renren.wap.server.xml.dto.node.item.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.renren.wap.server.xml.dto.node.item.data.location.LocationDetailDTO;

import java.io.Serializable;

/**
 * 数据区域信息
 *
 * @Author: CalmLake
 * @Date: 2018/11/20  11:48
 * @Version: V1.0.0
 **/
@XStreamAlias("DataArea")
public class LoadUnitAtIDDataDTO implements Serializable {

    @XStreamAlias("RouteChangeReguest")
    private String routeChangeReguest;
    @XStreamAlias("WcsLoadId")
    private String wcsLoadId;
    @XStreamAlias("Location")
    private LocationDetailDTO locationDetailDTO;
    @XStreamAlias("Weight")
    private String weight;
    @XStreamAlias("ErrorCode")
    private String errorCode;
    @XStreamAlias("Information")
    private String information;
    @XStreamAlias("ScanData")
    private String scanDate;
    @XStreamAlias("LoadType")
    private String loadType;

    public String getWcsLoadId() {
        return wcsLoadId;
    }

    public void setWcsLoadId(String wcsLoadId) {
        this.wcsLoadId = wcsLoadId;
    }

    public LocationDetailDTO getLocationDetailDTO() {
        return locationDetailDTO;
    }

    public void setLocationDetailDTO(LocationDetailDTO locationDetailDTO) {
        this.locationDetailDTO = locationDetailDTO;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public String getRouteChangeReguest() {
        return routeChangeReguest;
    }

    public void setRouteChangeReguest(String routeChangeReguest) {
        this.routeChangeReguest = routeChangeReguest;
    }
}
