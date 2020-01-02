package io.renren.wap.enums;


import io.renren.wap.entity.constant.BlockConstant;

/**
 * block 任务标识的类型
 *
 * @Author: CalmLake
 * @Date: 2019/7/25  15:26
 * @Version: V1.0.0
 **/
public enum BlockKeyTypeEnum {
    /**
     * block中key的四种不同状态
     */
    KEY_NOT_EMPTY_STRING(BlockConstant.KEY_NOT_EMPTY_STRING, BlockConstant.KEY_NOT_EMPTY),
    MCKEY_NOT_EMPTY_STRING(BlockConstant.MCKEY_NOT_EMPTY_STRING, BlockConstant.MCKEY_NOT_EMPTY),
    APPOINTMENT_MCKEY_NOT_EMPTY_STRING(BlockConstant.APPOINTMENT_MCKEY_NOT_EMPTY_STRING, BlockConstant.APPOINTMENT_MCKEY_NOT_EMPTY),
    KEY_EMPTY_STRING(BlockConstant.KEY_EMPTY_STRING, BlockConstant.KEY_EMPTY);
    /**
     * key的字符串类型
     */
    private String typeString;
    /**
     * key的整数类型
     */
    private int typeInt;

    BlockKeyTypeEnum(String typeString, int typeInt) {
        this.typeString = typeString;
        this.typeInt = typeInt;
    }

    public int getTypeInt() {
        return typeInt;
    }
}
