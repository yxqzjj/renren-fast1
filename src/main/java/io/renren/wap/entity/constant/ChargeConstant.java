package io.renren.wap.entity.constant;

/**
 * 充电信息
 *
 * @Author: CalmLake
 * @Date: 2019/3/20  11:12
 * @Version: V1.0.0
 **/
public class ChargeConstant {
    /**
     * 充电位在设备
     */
    public static final Byte TYPE_MACHINE = 1;
    /**
     * 充电位在绑定它的堆垛机或者母车可以到达货架
     */
    public static final Byte TYPE_AT_PRESENT_MACHINE = 2;
    /**
     * 充电位在另一堆垛机或者母车可以到达货架
     */
    public static final Byte TYPE_OTHER_ONR_MACHINE = 3;
}
