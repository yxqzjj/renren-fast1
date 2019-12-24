package io.renren.wcs.client.init;

import io.renren.wcs.client.init.Thread.InitThread;
import io.renren.wcs.threadPool.ThreadPoolServiceSingleton;
import io.renren.wcs.util.Log4j2Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ：zzwy
 * @date ：Created in 2019/12/20 12:01
 * @description：服务初始化
 * @modified By：
 * @version: 1.0
 **/
@Component
public class init {

    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * @author     ：zzwy
     * @date       ：2019/12/20
     * @description：初始化服务类
     * @modified By：
     * @version: 1.0
     */
    @PostConstruct
    public void init()
    {
        Log4j2Util.getRoot().info("初始化启动类。。。。。。。。。。。。");
        InitThread initThread  = new InitThread();
        //1.启动初始化服务线程
        ThreadPoolServiceSingleton.getInstance().getDefaultThreadFactory().newThread(initThread).start();
    }
}
