package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsScblockEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 穿梭车运行信息
 *
 * @author CalmLake
 * @date 2019/1/9 14:28
 */
@Repository("SCBlockDao")
@Mapper
public interface SCBlockDao extends BlockDao {
    @Delete({
            "delete from WCS_SCBlock",
            "where  NAME = #{name,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String name);

    @Insert({
            "insert into WCS_SCBlock (Name, McKey, Appointment_McKey, ",
            "Command, Error_Code, ",
            "Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, Host_Block_Name, ",
            "KWH, Last_Work_Time, ",
            "Row, Line, Tier, ",
            "Reserved1, Reserved2,Is_Standby_Car,Is_Use)",
            "values (#{name,jdbcType=CHAR}, #{mcKey,jdbcType=CHAR}, #{appointmentMcKey,jdbcType=CHAR}, ",
            "#{command,jdbcType=NCHAR}, #{errorCode,jdbcType=NCHAR}, ",
            "#{status,jdbcType=NCHAR}, #{isLoad,jdbcType=BIT}, #{withWorkBlockName,jdbcType=CHAR}, ",
            "#{berthBlockName,jdbcType=CHAR}, #{hostBlockName,jdbcType=CHAR}, ",
            "#{kwh,jdbcType=CHAR}, #{lastWorkTime,jdbcType=TIMESTAMP}, ",
            "#{row,jdbcType=CHAR}, #{line,jdbcType=CHAR}, #{tier,jdbcType=CHAR}, ",
            "#{reserved1,jdbcType=VARCHAR}, #{reserved2,jdbcType=VARCHAR}, #{isStandbyCar,jdbcType=BIT}, #{isUse,jdbcType=BIT})"
    })
    int insert(WcsScblockEntity record);

    /**
     * 获取block信息列表
     *
     * @return java.util.List<com.wap.entity.ScBlock>
     * @author CalmLake
     * @date 2019/2/22 11:33
     */
    @Select({
            "select",
            " * ",
            "from WCS_SCBlock"
    })
    @Results(id = "scBlock", value = {
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "McKey", property = "mcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Appointment_McKey", property = "appointmentMcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Command", property = "command", jdbcType = JdbcType.NCHAR),
            @Result(column = "Error_Code", property = "errorCode", jdbcType = JdbcType.NCHAR),
            @Result(column = "Status", property = "status", jdbcType = JdbcType.NCHAR),
            @Result(column = "Is_Load", property = "isLoad", jdbcType = JdbcType.BIT),
            @Result(column = "With_Work_Block_Name", property = "withWorkBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Berth_Block_Name", property = "berthBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Host_Block_Name", property = "hostBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "KWH", property = "kwh", jdbcType = JdbcType.CHAR),
            @Result(column = "Last_Work_Time", property = "lastWorkTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Row", property = "row", jdbcType = JdbcType.CHAR),
            @Result(column = "Line", property = "line", jdbcType = JdbcType.CHAR),
            @Result(column = "Tier", property = "tier", jdbcType = JdbcType.CHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Is_Standby_Car", property = "isStandbyCar", jdbcType = JdbcType.BIT),
            @Result(column = "Is_Use", property = "isUse", jdbcType = JdbcType.BIT)
    })
    List<WcsScblockEntity> getSCBlockList();

    /**
     * 获取穿梭车名称列表
     *
     * @return java.util.List<java.lang.String>
     * @author CalmLake
     * @date 2019/4/19 11:59
     */
    @Select({
            "select",
            " Name ",
            "from WCS_SCBlock"
    })
    @Results({
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true)
    }
    )
    List<String> getSCBlockNameList();

    /**
     * 穿梭车：根据数据block名称查询所有字段信息
     *
     * @param name 数据block名称
     * @return com.wap.entity.ScBlock
     * @author CalmLake
     * @date 2019/1/4 17:28
     */
    @Override
    @Select({
            "select",
            " * ",
            "from WCS_SCBlock",
            "where  NAME = #{name,jdbcType=CHAR}"
    })
    @ResultMap("scBlock")
    WcsScblockEntity selectByPrimaryKey(String name);

    /**
     * 查找当前子车列表中是否为备车的设备
     *
     * @param isStandbyCar 是否是备车
     * @return java.util.List<com.wap.entity.ScBlock>
     * @author CalmLake
     * @date 2019/4/18 11:26
     */
    @Select({
            "select",
            " * ",
            "from WCS_SCBlock",
            "where Is_Standby_Car = #{isStandbyCar,jdbcType=BIT}"
    })
    @ResultMap("scBlock")
    List<WcsScblockEntity> getScBlockListByStandbyCar(@Param("isStandbyCar") boolean isStandbyCar);

    @Update({
            "update WCS_SCBlock",
            "set \"McKey\" = #{mcKey,jdbcType=CHAR},",
            "\"Appointment_McKey\" = #{appointmentMcKey,jdbcType=CHAR},",
            "\"Command\" = #{command,jdbcType=NCHAR},",
            "\"Error_Code\" = #{errorCode,jdbcType=NCHAR},",
            "\"Status\" = #{status,jdbcType=NCHAR},",
            "\"Is_Load\" = #{isLoad,jdbcType=BIT},",
            "\"With_Work_Block_Name\" = #{withWorkBlockName,jdbcType=CHAR},",
            "\"Berth_Block_Name\" = #{berthBlockName,jdbcType=CHAR},",
            "\"Host_Block_Name\" = #{hostBlockName,jdbcType=CHAR},",
            "\"KWH\" = #{kwh,jdbcType=CHAR},",
            "\"Last_Work_Time\" = #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"Row\" = #{row,jdbcType=CHAR},",
            "\"Line\"= #{line,jdbcType=CHAR},",
            "\"Tier\" = #{tier,jdbcType=CHAR},",
            "\"Reserved1\" = #{reserved1,jdbcType=VARCHAR},",
            "\"Reserved2\" = #{reserved2,jdbcType=VARCHAR},",
            "\"Is_Standby_Car\" = #{isStandbyCar,jdbcType=BIT},",
            "\"Is_Use\" = #{isUse,jdbcType=BIT}",
            "where NAME = #{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsScblockEntity record);

    @Update({
            "update WCS_SCBlock",
            "set \"RESERVED2\"=#{reserved2,jdbcType=VARCHAR}",
            "where  NAME = #{name,jdbcType=CHAR}"
    })
    int updateReserved2ByName(@Param("name") String name, @Param("reserved2") String reserved2);

    /**
     * 修改mcKey,appointmentMcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param appointmentMcKey  appointmentMcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/24 11:08
     */
    @Override
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"APPOINTMENT_MCKEY\" =  #{appointmentMcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\" =  #{withWorkBlockName,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateThreeValueBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

    /**
     * 为设备添加预约任务
     *
     * @param name             数据block名称
     * @param appointmentMcKey 预约任务标识
     * @param reserved1        预约交互设备名称
     * @return int
     * @author CalmLake
     * @date 2019/3/20 12:06
     */
    @Override
    @Update({
            "update WCS_SCBlock",
            "set  \"APPOINTMENT_MCKEY\" =   #{appointmentMcKey,jdbcType=CHAR},\"RESERVED1\" = #{reserved1,jdbcType=VARCHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKeyReserved1ByName(@Param("name") String name, @Param("appointmentMcKey") String appointmentMcKey, @Param("reserved1") String reserved1);

    /**
     * 修改设备的使用状态
     *
     * @param name  数据block名称
     * @param isUse 是否使用
     * @return int
     * @author CalmLake
     * @date 2019/4/18 15:03
     */
    @Update({
            "update WCS_SCBlock",
            "set \"IS_USE\" =  #{isUse,jdbcType=BIT} ",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateIsUseByName(@Param("name") String name, @Param("isUse") boolean isUse);


    /**
     * 修改设备的状态
     *
     * @param name   数据block名称
     * @param status 状态
     * @return int
     * @author CalmLake
     * @date 2019/3/19 16:54
     */
    @Update({
            "update WCS_SCBlock",
            "set \"STATUS\" =  #{status,jdbcType=NCHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateStatus(@Param("name") String name, @Param("status") String status);

    /**
     * 修改设备的寄宿状态
     *
     * @param name          数据block名称
     * @param hostBlockName 宿主设备名称
     * @return int
     * @author CalmLake
     * @date 2019/3/5 13:57
     */
    @Update({
            "update WCS_SCBlock",
            "set \"HOST_BLOCK_NAME\" =  #{hostBlockName,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateHostName(@Param("name") String name, @Param("hostBlockName") String hostBlockName);

    /**
     * 修改设备的载荷状态
     *
     * @param name   数据block名称
     * @param isLoad 载荷状态
     * @return int
     * @author CalmLake
     * @date 2019/3/5 13:57
     */
    @Update({
            "update WCS_SCBlock",
            "set  \"IS_LOAD\" = #{isLoad,jdbcType=BIT}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateLoad(@Param("name") String name, @Param("isLoad") boolean isLoad);

    /**
     * 修改设备的载荷状态
     *
     * @param name   数据block名称
     * @param isLoad 载荷状态
     * @param mcKey  任务标识
     * @return int
     * @author CalmLake
     * @date 2019/3/5 13:57
     */
    @Update({
            "update WCS_SCBlock",
            "set  \"MCKEY\" = #{mcKey,jdbcType=CHAR},\"IS_LOAD\" = #{isLoad,jdbcType=BIT}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateLoadMcKey(@Param("name") String name, @Param("isLoad") boolean isLoad, @Param("mcKey") String mcKey);

    /**
     * 修改当前执行命令类型和载荷状态
     *
     * @param name    数据block名称
     * @param command 命令类型
     * @param isLoad  载荷状态
     * @return int
     * @author CalmLake
     * @date 2019/1/16 9:41
     */
    @Update({
            "update WCS_SCBlock",
            "set \"COMMAND\" =  #{command,jdbcType=NCHAR},\"IS_LOAD\" = #{isLoad,jdbcType=BIT} ",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateCommandAndLoad(@Param("name") String name, @Param("command") String command, @Param("isLoad") boolean isLoad);

    /**
     * 修改mcKey,appointmentMcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param appointmentMcKey  appointmentMcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/9 13:47
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"APPOINTMENT_MCKEY\" =  #{appointmentMcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\" =  #{withWorkBlockName,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateThreeScBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

    /**
     * 修改mcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/23 9:25
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\" =  #{withWorkBlockName,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateTwoScBlock(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

    /**
     * 修改mcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/23 9:25
     */
    @Override
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\" =  #{withWorkBlockName,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateMcKeyByName(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

    /**
     * 修改mcKey
     *
     * @param mcKey mcKey
     * @param name  数据block编号
     * @return int
     * @author CalmLake
     * @date 2019/2/22 16:46
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateMcKey(@Param("mcKey") String mcKey, @Param("name") String name);

    /**
     * 修改appointmentMcKey
     *
     * @param appointmentMcKey 预约mcKey
     * @param name             数据block编号
     * @return int
     * @author CalmLake
     * @date 2019/2/22 16:47
     */
    @Update({
            "update WCS_SCBlock",
            "set ",
            "\"APPOINTMENT_MCKEY\" =  #{appointmentMcKey,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKey(@Param("appointmentMcKey") String appointmentMcKey, @Param("name") String name);

    /**
     * 移动完成
     *
     * @param name         数据block名称
     * @param lastWorkTime 最后一次工作完成时间
     * @param row          排
     * @param line         列
     * @param tier         层
     * @return int
     * @author CalmLake
     * @date 2019/4/10 9:56
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = '',",
            "\"COMMAND\" =  '35',",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR} ",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateMoveFinishByPrimaryKey(@Param("name") String name, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    /**
     * 盘点任务完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 16:33
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" ='',",
            "\"COMMAND\" =  '35',",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateTakeStockFinishByPrimaryKey(@Param("name") String name, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);


    /**
     * 理货任务完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 16:27
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" ='',",
            "\"COMMAND\" =  '35',",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateTallyFinishByPrimaryKey(@Param("name") String name, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);


    /**
     * 充电完成完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    @Update({
            "update WCS_SCBlock",
            "set ",
            "\"COMMAND\" =  '35',",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR},Berth_Block_Name = 'location'",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateChargeFinishByPrimaryKey(@Param("name") String name, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    /**
     * 充电开始任务完成
     *
     * @param name         该设备数据block
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/4/12 14:18
     */
    @Update({
            "update WCS_SCBlock",
            "set ",
            "\"COMMAND\" =  '35',\"STATUS\" = 4,\"MCKEY\" = '',",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},Berth_Block_Name = 'charge',\"IS_USE\" =  0 ",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateChargeStartByPrimaryKey(@Param("name") String name, @Param("lastWorkTime") Date lastWorkTime);

    /**
     * 子车载货下车完成
     *
     * @param name          数据block名称
     * @param hostBlockName 宿主数据block名称
     * @param row           排
     * @param line          列
     * @param tier          层
     * @param isLoad        载货状态
     * @param lastWorkTime  最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    @Update({
            "update WCS_SCBLOCK",
            "set ",
            "\"COMMAND\" = '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"HOST_BLOCK_NAME\" = #{hostBlockName,jdbcType=CHAR},",
            "\"LAST_WORK_TIME\" = #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" = #{row,jdbcType=CHAR},",
            "\"LINE\" = #{line,jdbcType=CHAR},\"WITH_WORK_BLOCK_NAME\" = '',",
            "\"TIER\" = #{tier,jdbcType=CHAR}",
            "where  Name = #{name,jdbcType=CHAR}"
    })
    int updateOffCarFinishByPrimaryKey(@Param("name") String name, @Param("hostBlockName") String hostBlockName, @Param("isLoad") boolean isLoad, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    /**
     * 子车载货上车完成
     *
     * @param name          数据block名称
     * @param mcKey         任务标识
     * @param hostBlockName 宿主数据block名称
     * @param row           排
     * @param line          列
     * @param tier          层
     * @param isLoad        载货状态
     * @param lastWorkTime  最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"COMMAND\" =  '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"HOST_BLOCK_NAME\" =  #{hostBlockName,jdbcType=CHAR},",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateGoOnCarFinishByPrimaryKey(@Param("name") String name, @Param("mcKey") String mcKey, @Param("hostBlockName") String hostBlockName, @Param("isLoad") boolean isLoad, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    /**
     * 子车空车下车完成
     *
     * @param name          数据block名称
     * @param hostBlockName 宿主数据block名称
     * @param row           排
     * @param line          列
     * @param tier          层
     * @param isLoad        载货状态
     * @param lastWorkTime  最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    @Update({
            "update WCS_SCBlock",
            "set ",
            "\"COMMAND\" =  '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"HOST_BLOCK_NAME\" =  #{hostBlockName,jdbcType=CHAR},",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateOffCarEmptyFinishByPrimaryKey(@Param("name") String name, @Param("hostBlockName") String hostBlockName, @Param("isLoad") boolean isLoad, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    @Update({
            "update WCS_SCBlock",
            "set ",
            "\"COMMAND\" =  '35',",
            "\"HOST_BLOCK_NAME\" =  #{hostBlockName,jdbcType=CHAR},",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR},Berth_Block_Name=#{berthBlockName,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateOffCarToClAEmptyFinishByPrimaryKey(@Param("name") String name, @Param("hostBlockName") String hostBlockName, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier, @Param("berthBlockName") String berthBlockName);

    @Update({
            "update WCS_SCBlock",
            "set ",
            "Berth_Block_Name=#{berthBlockName,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateBerthBlockNameByPrimaryKey(@Param("name") String name, @Param("berthBlockName") String berthBlockName);

    /**
     * 子车空车上车完成
     *
     * @param name             数据block名称
     * @param appointmentMcKey 预约任务标识
     * @param hostBlockName    宿主数据block名称
     * @param row              排
     * @param line             列
     * @param tier             层
     * @param isLoad           载货状态
     * @param lastWorkTime     最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    @Update({
            "update WCS_SCBlock",
            "set \"APPOINTMENT_MCKEY\" =  #{appointmentMcKey,jdbcType=CHAR},",
            "\"COMMAND\" =  '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"HOST_BLOCK_NAME\" =  #{hostBlockName,jdbcType=CHAR},",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateGoOnCarEmptyFinishAppointmentMcKeyByPrimaryKey(@Param("name") String name, @Param("appointmentMcKey") String appointmentMcKey, @Param("hostBlockName") String hostBlockName, @Param("isLoad") boolean isLoad, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"COMMAND\" =  '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"HOST_BLOCK_NAME\" =  #{hostBlockName,jdbcType=CHAR},",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateGoOnCarEmptyFinishByPrimaryKey(@Param("name") String name, @Param("mcKey") String mcKey, @Param("hostBlockName") String hostBlockName, @Param("isLoad") boolean isLoad, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    /**
     * 子车卸货完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param isLoad       载货状态
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:09
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = '',",
            "\"COMMAND\" = '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"LAST_WORK_TIME\" = #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"HOST_BLOCK_NAME\" =  '',",
            "\"ROW\" = #{row,jdbcType=CHAR},",
            "\"LINE\" = #{line,jdbcType=CHAR},",
            "\"TIER\" = #{tier,jdbcType=CHAR} ",
            "where  NAME= #{name,jdbcType=CHAR}"
    })
    int updateUnloadJiaTianFinishByPrimaryKey(@Param("name") String name, @Param("isLoad") boolean isLoad, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);


    /**
     * 子车卸货完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param isLoad       载货状态
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:09
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = '',",
            "\"COMMAND\" = '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"LAST_WORK_TIME\" = #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" = #{row,jdbcType=CHAR},",
            "\"LINE\" = #{line,jdbcType=CHAR},",
            "\"TIER\" = #{tier,jdbcType=CHAR} ",
            "where  NAME= #{name,jdbcType=CHAR}"
    })
    int updateUnloadFinishByPrimaryKey(@Param("name") String name, @Param("isLoad") boolean isLoad, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    /**
     * 子车取货完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param isLoad       载货状态
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:09
     */
    @Update({
            "update WCS_SCBlock",
            "set ",
            "\"COMMAND\" =  '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR} ",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updatePickUpFinishByPrimaryKey(@Param("name") String name, @Param("isLoad") boolean isLoad, @Param("lastWorkTime") Date lastWorkTime, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    /**
     * 修改子车位置信息
     *
     * @param name 数据block名称
     * @param row  排
     * @param line 列
     * @param tier 层
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:16
     */
    @Update({
            "update WCS_SCBlock",
            "set ",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR} ",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateLocationByPrimaryKey(@Param("name") String name, @Param("row") String row, @Param("line") String line, @Param("tier") String tier);

    /**
     * 修改穿梭车排列层和载荷信息
     *
     * @param name   block名称
     * @param row    排
     * @param line   列
     * @param tier   层
     * @param isLoad 载荷状态
     * @return int
     * @author CalmLake
     * @date 2019/5/26 13:59
     */
    @Update({
            "UPDATE \"WCS_SCBLOCK\" SET  \"IS_LOAD\" = #{isLoad,jdbcType=BIT},  \"ROW\" = #{row,jdbcType=CHAR}, \"LINE\" = #{line,jdbcType=CHAR}, \"TIER\" = #{tier,jdbcType=CHAR}" +
                    " WHERE \"NAME\" = #{name,jdbcType=CHAR} "
    })
    int updateLocationLoadByPrimaryKey(@Param("name") String name, @Param("row") String row, @Param("line") String line, @Param("tier") String tier, @Param("isLoad") boolean isLoad);

    /**
     * 修改当前执行命令类型
     *
     * @param name    数据block名称
     * @param command 命令类型
     * @return int
     * @author CalmLake
     * @date 2019/1/16 9:41
     */
    @Override
    @Update({
            "update WCS_SCBlock",
            "set \"COMMAND\" =  #{command,jdbcType=NCHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateCommandByPrimaryKey(@Param("name") String name, @Param("command") String command);

    /**
     * 修改当前异常字段
     *
     * @param name      数据block名称
     * @param errorCode 异常码
     * @return int
     * @author CalmLake
     * @date 2019/1/16 16:57
     */
    @Update({
            "update WCS_SCBlock",
            "set \"ERROR_CODE\"  = #{errorCode,jdbcType=NCHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateBlockErrorCodeByPrimaryKey(@Param("name") String name, @Param("errorCode") String errorCode);

    /**
     * 修改当前异常字段和电量
     *
     * @param name      数据block名称
     * @param errorCode 异常码
     * @param kwh       电量
     * @return int
     * @author CalmLake
     * @date 2019/1/16 16:57
     */
    @Update({
            "update WCS_SCBlock",
            "set \"ERROR_CODE\" =  #{errorCode,jdbcType=NCHAR},",
            "\"KWH\" =  #{kwh,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    int updateBlockErrorCodeAndKWHByPrimaryKey(@Param("kwh") String kwh, @Param("name") String name, @Param("errorCode") String errorCode);
    /**
     *一键恢复设备状态
     * @return int
     * @author CalmLake
     * @date 2019/11/20 9:03
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" = '',",
            "\"APPOINTMENT_MCKEY\" = '',",
            "\"COMMAND\" = '05',",
            "\"ERROR_CODE\" = '00',",
            "\"STATUS\" = '1',",
            "\"IS_LOAD\" = '0',",
            "\"WITH_WORK_BLOCK_NAME\" = '',",
            "\"BERTH_BLOCK_NAME\"= 'ML01',",
            "\"HOST_BLOCK_NAME\" = 'ML01',",
            "\"ROW\"= '00',",
            "\"LINE\" = '00',",
            "\"TIER\" = '00',",
            "\"RESERVED1\" = '',",
            "\"RESERVED2\" = '',",
            "\"IS_STANDBY_CAR\" = '0'",
            " where NAME = #{name,jdbcType=CHAR} "
    })
    int updateOneRecovery(@Param("name") String name);
}