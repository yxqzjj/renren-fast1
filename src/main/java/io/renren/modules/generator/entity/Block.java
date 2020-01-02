package io.renren.modules.generator.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * block状态父类
 *
 * @Author: CalmLake
 * @date 2019/7/25  12:06
 * @Version: V1.0.0
 **/
@Data
public class Block implements Serializable {
    /**
     * block名称
     */
    private String name;
    /**
     * 货物搬送唯一标识
     */
    private String mckey;
    /**
     * 预约搬运货物唯一标识
     */
    private String appointmentMckey;
    /**
     * 当前执行命令
     */
    /**
     * 当前执行命令种类
     */
    private String command;
    /**
     * 故障代码
     */
    private String errorCode;
    /**
     * 状态
     */
    private String status;
    /**
     * 载荷状态
     */
    private Boolean isLoad;
    /**
     * 当前交互设备block名称
     */
    private String withWorkBlockName;
    /**
     * 当前停泊设备block名称
     */
    private String berthBlockName;
    /**
     * 预约任务交互设备block名称
     */
    private String reserved1;

    private String reserved2;
}
