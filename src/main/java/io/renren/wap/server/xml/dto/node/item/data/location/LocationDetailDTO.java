package io.renren.wap.server.xml.dto.node.item.data.location;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * 位置详细信息
 *
 * @Author: CalmLake
 * @Date: 2018/11/20  15:21
 * @Version: V1.0.0
 **/
@XStreamAlias("Location")
public class LocationDetailDTO implements Serializable {
    @XStreamAlias("MHA")
    private String mha;
    @XStreamImplicit(itemFieldName = "Rack")
    private List<String> rack;

    public String getMha() {
        return mha;
    }

    public void setMha(String mha) {
        this.mha = mha;
    }

    public List<String> getRack() {
        return rack;
    }

    public void setRack(List<String> rack) {
        this.rack = rack;
    }
}
