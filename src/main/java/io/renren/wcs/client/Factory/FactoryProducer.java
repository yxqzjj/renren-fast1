package io.renren.wcs.client.Factory;


/**
 * 工厂生产
 * @Author: CalmLake
 * @Date: 2018/11/17  17:44
 * @Version: V1.0.0
 **/
public class FactoryProducer {
    /**
     * 获取工厂实例对象
     * @param choice 选择标识
     * @return factory
     */
    public static AbstractFactory getFactory(String choice){
        if(FactoryConstant.SEND.equals(choice)){
            return new MsgSendServiceFactory();
        } else if(FactoryConstant.RECEIVE.equals(choice)){
            return new MsgReceiveServiceFactory();
        }
        return null;
    }
}
