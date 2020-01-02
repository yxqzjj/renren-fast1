package io.renren.wap.service.charge;

/**
 * 充电
 *
 * @Author: CalmLake
 * @Date: 2019/3/19  16:58
 * @Version: V1.0.0
 **/
public interface ChargeInterface {
    /**
     * 充电开始
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/3/19 17:00
     */
    void startCharge(String blockName);

    /**
     * 充电完成
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/3/20 10:12
     */
    void finishCharge(String blockName);

    /**
     * 低电量处理
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/3/20 10:13
     */
    void lowPower(String blockName);
}
