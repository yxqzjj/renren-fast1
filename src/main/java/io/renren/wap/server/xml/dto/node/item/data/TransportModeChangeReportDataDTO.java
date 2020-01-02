package io.renren.wap.server.xml.dto.node.item.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 数据区域信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:52
 * @Version: V1.0.0
 **/
@XStreamAlias("DataArea")
public class TransportModeChangeReportDataDTO implements Serializable {
    @XStreamAlias("TransportType")
    private String transportType;
    @XStreamAlias("MHA")
    private String mha;
    @XStreamAlias("Information")
    private String information;

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getMha() {
        return mha;
    }

    public void setMha(String mha) {
        this.mha = mha;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
