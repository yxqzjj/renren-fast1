package io.renren.wap.server.xml.dto.node.item.data.location;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * @ClassName: LocationDTO
 * @Description: 位置
 * @Author: CalmLake
 * @Date: 2018/11/20  15:10
 * @Version: V1.0.0
 **/
@XStreamAlias("Location")
public class LocationDTO implements Serializable {
    @XStreamAlias("MHA")
    private String mha;

    public String getMha() {
        return mha;
    }

    public void setMha(String mha) {
        this.mha = mha;
    }
}
