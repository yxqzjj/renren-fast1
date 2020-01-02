package io.renren.wap.server.xml.dto.node.item.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 数据区域信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:51
 * @Version: V1.0.0
 **/
@XStreamAlias("DataArea")
public class QueryTransportModeDataDTO implements Serializable {
    @XStreamAlias("MHA")
    private String mha;

    public String getMha() {
        return mha;
    }

    public void setMha(String mha) {
        this.mha = mha;
    }
}
