package io.renren.wap.util;


import io.renren.wap.yml.LogConfigInfo;
import io.renren.wap.yml.SystemConfigInfo;
import io.renren.wap.yml.WmsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/3/29  17:43
 * @Version: V1.0.0
 **/
@Repository("YmlReadUtil")
public class YmlReadUtil {
    /**
     * wms配置信息
     */
    @Autowired
    private WmsInfo wmsInfo;
    @Autowired
    private SystemConfigInfo systemConfigInfo;
    @Autowired
    private LogConfigInfo logConfigInfo;

    public WmsInfo getWmsInfo() {
        return wmsInfo;
    }

    public SystemConfigInfo getSystemConfigInfo() {
        return systemConfigInfo;
    }

    public LogConfigInfo getLogConfigInfo() {
        return logConfigInfo;
    }
}
