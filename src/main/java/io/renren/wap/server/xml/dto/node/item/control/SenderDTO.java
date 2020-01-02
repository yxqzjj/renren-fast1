package io.renren.wap.server.xml.dto.node.item.control;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * @ClassName: SenderDTO
 * @Description: XML 发送者
 * @Author: CalmLake
 * @Date: 2018/11/20  11:35
 * @Version: V1.0.0
 **/
@XStreamAlias("Sender")
public class SenderDTO implements Serializable {
    @XStreamAlias("Division")
    private String division;

    public String getDivision() {
        return this.division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
