package io.renren.wcs.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ：zzwy
 * @date ：Created in 2019/12/23 8:59
 * @description：通过applicationContext从容器中获取bean
 * @modified By：
 * @version: 1.0
 */
@Component
public class SpringBootBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
                   if(SpringBootBeanUtil.applicationContext == null){
                       SpringBootBeanUtil.applicationContext = applicationContext;
                   }
    }
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}
