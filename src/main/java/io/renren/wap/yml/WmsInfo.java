package io.renren.wap.yml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * wms配置信息
 *
 * @Author: CalmLake
 * @Date: 2019/3/29  16:37
 * @Version: V1.0.0
 **/
@Component
@PropertySource("classpath:WmsInfo.yml")
@ConfigurationProperties(prefix = "wms")
public class WmsInfo {
    /**
     * 名称
     */
    private String name;
    /**
     * 端口号
     */
    private int port;
    /**
     * LoadUnitAtID消息重发时间间隔 秒
     */
    private int resendLoadUnitAtIDTimeInterval;
    /**
     * LoadUnitAtID消息重发最大次数
     */
    private int resendLoadUnitAtIDMax;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getResendLoadUnitAtIDTimeInterval() {
        return resendLoadUnitAtIDTimeInterval;
    }

    public void setResendLoadUnitAtIDTimeInterval(int resendLoadUnitAtIDTimeInterval) {
        this.resendLoadUnitAtIDTimeInterval = resendLoadUnitAtIDTimeInterval;
    }

    public int getResendLoadUnitAtIDMax() {
        return resendLoadUnitAtIDMax;
    }

    public void setResendLoadUnitAtIDMax(int resendLoadUnitAtIDMax) {
        this.resendLoadUnitAtIDMax = resendLoadUnitAtIDMax;
    }
}
