package io.renren.wap.yml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * log操作信息
 *
 * @Author: CalmLake
 * @Date: 2019/4/24  11:06
 * @Version: V1.0.0
 **/
@Component
@PropertySource("classpath:LogConfigInfo.yml")
@ConfigurationProperties(prefix = "log-config")
public class LogConfigInfo {
    /**
     * log日志记录路径
     */
    private String logsPath;
    /**
     * 自动删除过期日志开关
     */
    private boolean deleteLogSwitch;
    /**
     * 日志保留天数
     */
    private int logReserveTime;

    public String getLogsPath() {
        return logsPath;
    }

    public void setLogsPath(String logsPath) {
        this.logsPath = logsPath;
    }

    public boolean isDeleteLogSwitch() {
        return deleteLogSwitch;
    }

    public void setDeleteLogSwitch(boolean deleteLogSwitch) {
        this.deleteLogSwitch = deleteLogSwitch;
    }

    public int getLogReserveTime() {
        return logReserveTime;
    }

    public void setLogReserveTime(int logReserveTime) {
        this.logReserveTime = logReserveTime;
    }
}
