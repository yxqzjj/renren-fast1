package io.renren.wap.dao;


import io.renren.modules.generator.entity.Block;

/**
 * **blockDao 父类
 *
 * @Author: CalmLake
 * @Date: 2019/7/25  14:32
 * @Version: V1.0.0
 **/
public interface BlockDao {
    /**
     * 获取block数据
     *
     * @param name 数据block名称
     * @return com.wap.entity.BlockImpl
     * @author CalmLake
     * @date 2019/7/25 14:33
     */
    Block selectByPrimaryKey(String name);

    /**
     * 修改mcKey,appointmentMcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param appointmentMcKey  appointmentMcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/8/21 12:41
     */
    int updateThreeValueBlock(String mcKey, String appointmentMcKey, String withWorkBlockName, String name);

    /**
     * 分配预约任务
     *
     * @param name             block名称
     * @param appointmentMcKey 任务标识
     * @param reserved1        交互设备名称
     * @return int
     * @author CalmLake
     * @date 2019/7/26 14:46
     */
    int updateAppointmentMcKeyReserved1ByName(String name, String appointmentMcKey, String reserved1);

    /**
     * 分配任务
     *
     * @param mcKey             任务标识
     * @param withWorkBlockName 交互设备名称
     * @param name              block名称
     * @return int
     * @author CalmLake
     * @date 2019/7/26 14:53
     */
    int updateMcKeyByName(String mcKey, String withWorkBlockName, String name);

    /**
     * 修改block命令状态
     *
     * @param name    数据block
     * @param command 命令
     * @return int
     * @author CalmLake
     * @date 2019/8/15 10:19
     */
    int updateCommandByPrimaryKey(String name, String command);
}
