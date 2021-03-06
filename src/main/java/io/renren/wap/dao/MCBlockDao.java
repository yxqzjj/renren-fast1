package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsMcblockEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 母车运行信息
 *
 * @author CalmLake
 * @date 2019/1/9 14:24
 */
@Repository("MCBlockDao")
@Mapper
public interface MCBlockDao extends BlockDao {
    @Delete({
            "delete from WCS_MCBlock",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String name);

    @Insert({
            "insert into WCS_MCBlock (Name, McKey, Appointment_McKey, ",
            "Command, Error_Code, ",
            "Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, SC_Block_Name, ",
            "Bing_SC_Block_Name, Row, ",
            "Line, Tier, Reserved1, ",
            "Reserved2)",
            "values (#{name,jdbcType=CHAR}, #{mcKey,jdbcType=CHAR}, #{appointmentMcKey,jdbcType=CHAR}, ",
            "#{command,jdbcType=NCHAR}, #{errorCode,jdbcType=NCHAR}, ",
            "#{status,jdbcType=NCHAR}, #{isLoad,jdbcType=BIT}, #{withWorkBlockName,jdbcType=CHAR}, ",
            "#{berthBlockName,jdbcType=CHAR}, #{scBlockName,jdbcType=CHAR}, ",
            "#{bingScBlockName,jdbcType=CHAR}, #{row,jdbcType=CHAR}, ",
            "#{line,jdbcType=CHAR}, #{tier,jdbcType=CHAR}, #{reserved1,jdbcType=VARCHAR}, ",
            "#{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsMcblockEntity record);

    /**
     * 获取母车block列表
     *
     * @return java.util.List<com.wap.entity.McBlock>
     * @author CalmLake
     * @date 2019/2/22 13:49
     */
    @Select({
            "select",
            " * ",
            "from WCS_MCBlock"
    })
    @Results(id = "mcBlock", value = {
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "McKey", property = "mcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Appointment_McKey", property = "appointmentMcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Command", property = "command", jdbcType = JdbcType.NCHAR),
            @Result(column = "Error_Code", property = "errorCode", jdbcType = JdbcType.NCHAR),
            @Result(column = "Status", property = "status", jdbcType = JdbcType.NCHAR),
            @Result(column = "Is_Load", property = "isLoad", jdbcType = JdbcType.BIT),
            @Result(column = "With_Work_Block_Name", property = "withWorkBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Berth_Block_Name", property = "berthBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "SC_Block_Name", property = "scBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Bing_SC_Block_Name", property = "bingScBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Row", property = "row", jdbcType = JdbcType.CHAR),
            @Result(column = "Line", property = "line", jdbcType = JdbcType.CHAR),
            @Result(column = "Tier", property = "tier", jdbcType = JdbcType.CHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Is_Standby_Car", property = "isStandbyCar", jdbcType = JdbcType.BIT),
            @Result(column = "Is_Move", property = "isMove", jdbcType = JdbcType.BIT),
            @Result(column = "Standby_Car_Block_Name", property = "standbyCarBlockName", jdbcType = JdbcType.CHAR)
    })
    List<WcsMcblockEntity> getMcBlockList();

    /**
     * 获取堆垛机名称列表
     *
     * @return java.util.List<java.lang.String>
     * @author CalmLake
     * @date 2019/4/19 11:59
     */
    @Select({
            "select",
            " Name ",
            "from WCS_MCBlock"
    })
    @Results(value = {
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true)
    })
    List<String> getMcBlockNameList();

    /**
     * 母车：根据数据block名称查询所有字段信息
     *
     * @param name 数据block名称
     * @return com.wap.entity.McBlock
     * @author CalmLake
     * @date 2019/1/4 17:28
     */
    @Override
    @Select({
            "select",
            " * ",
            "from WCS_MCBlock",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    @ResultMap("mcBlock")
    WcsMcblockEntity selectByPrimaryKey(String name);

    @Update({
            "update WCS_MCBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "Command = #{command,jdbcType=NCHAR},",
            "Error_Code = #{errorCode,jdbcType=NCHAR},",
            "Status = #{status,jdbcType=NCHAR},",
            "Is_Load = #{isLoad,jdbcType=BIT},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR},",
            "Berth_Block_Name = #{berthBlockName,jdbcType=CHAR},",
            "SC_Block_Name = #{scBlockName,jdbcType=CHAR},",
            "Bing_SC_Block_Name = #{bingScBlockName,jdbcType=CHAR},",
            "Row = #{row,jdbcType=CHAR},",
            "Line = #{line,jdbcType=CHAR},",
            "Tier = #{tier,jdbcType=CHAR},",
            "Reserved1 = #{reserved1,jdbcType=VARCHAR},",
            "Reserved2 = #{reserved2,jdbcType=VARCHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsMcblockEntity record);

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
            "update WCS_MCBlock",
            "set  Appointment_McKey =  #{appointmentMcKey,jdbcType=CHAR},Reserved1=#{reserved1,jdbcType=VARCHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKeyReserved1ByName(@Param("name") String name, @Param("appointmentMcKey") String appointmentMcKey, @Param("reserved1") String reserved1);

    /**
     * 修改设备所载设备名称
     *
     * @param name        数据block名称
     * @param scBlockName 子车名称
     * @return int
     * @author CalmLake
     * @date 2019/3/15 15:04
     */
    @Update({
            "update WCS_MCBlock",
            "set  SC_Block_Name = #{scBlockName,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateScBlockName(@Param("name") String name, @Param("scBlockName") String scBlockName);

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
            "update WCS_MCBlock",
            "set  Is_Load = #{isLoad,jdbcType=BIT}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateLoad(@Param("name") String name, @Param("isLoad") boolean isLoad);

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
            "update WCS_MCBlock",
            "set Command = #{command,jdbcType=NCHAR},Is_Load = #{isLoad,jdbcType=BIT} ",
            "where trim(Name) = #{name,jdbcType=CHAR}"
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
     * @date 2019/1/24 11:08
     */
    @Update({
            "update WCS_MCBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateThreeMcBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_MCBlock",
            "set McKey = #{mcKey,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
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
            "update WCS_MCBlock",
            "set ",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKey(@Param("appointmentMcKey") String appointmentMcKey, @Param("name") String name);

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
    @Update({
            "update WCS_MCBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateThreeValueMLBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);
    /**
     * 修改mcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/9 13:47
     */
    @Override
    @Update({
            "update WCS_MCBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateMcKeyByName(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_MCBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateThreeValueBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);
    /**
     * 堆垛机卸车完成
     *
     * @param name              数据block名称
     * @param scBlockName       子车block名称
     * @param isLoad            载货标识
     * @param mcKey             任务标识
     * @param withWorkBlockName 一起工作设备名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    @Update({
            "update WCS_MCBlock",
            "set McKey= #{mcKey,jdbcType=CHAR},",
            "Command = '35',",
            "Is_Load = #{isLoad,jdbcType=BIT},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR},",
            "SC_Block_Name = #{scBlockName,jdbcType=CHAR},Is_Move = 0 ",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateOffCarFinishByPrimaryKey(@Param("name") String name, @Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("scBlockName") String scBlockName, @Param("isLoad") boolean isLoad);

    /**
     * 堆垛机卸车完成 2 不修改任务标识
     *
     * @param name   数据block名称
     * @param isLoad 载货标识
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    @Update({
            "update WCS_MCBlock",
            "set ",
            "Command = '35',",
            "Is_Load = #{isLoad,jdbcType=BIT},",
            "SC_Block_Name = '',Is_Move = 0 ",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateOffCarFinish2ByPrimaryKey(@Param("name") String name, @Param("isLoad") boolean isLoad);

    /**
     * 堆垛机接车完成
     *
     * @param name        数据block名称
     * @param scBlockName 子车block名称
     * @param isLoad      载货标识
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    @Update({
            "update WCS_MCBlock",
            "set  ",
            "Command = '35',",
            "Is_Load = #{isLoad,jdbcType=BIT},",
            "SC_Block_Name = #{scBlockName,jdbcType=CHAR},Is_Move =0 ",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateGetCarFinishByPrimaryKey(@Param("name") String name, @Param("scBlockName") String scBlockName, @Param("isLoad") boolean isLoad);

    /**
     * 移动完成修改
     *
     * @param name           数据block名称
     * @param berthBlockName 停泊位置block名称
     * @param row            排
     * @param line           列
     * @param tier           层
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:04
     */
    @Update({
            "update WCS_MCBlock",
            "set ",
            "Command = '35',",
            "Berth_Block_Name = #{berthBlockName,jdbcType=CHAR},",
            "Row = #{row,jdbcType=CHAR},",
            "Line = #{line,jdbcType=CHAR},Is_Move = 1,",
            "Tier = #{tier,jdbcType=CHAR},Is_Load = #{loadStorage,jdbcType=BIT},SC_Block_Name = #{loadCar,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateMoveFinishByPrimaryKey(@Param("name") String name, @Param("berthBlockName") String berthBlockName, @Param("row") String row, @Param("line") String line, @Param("tier") String tier, @Param("loadStorage") boolean loadStorage, @Param("loadCar") String loadCar);

    /**
     * 移载取货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    @Update({
            "update WCS_MCBlock",
            "set Command = '35',",
            "Is_Load = 1,",
            "With_Work_Block_Name = '',SC_Block_Name = #{loadCar,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateBlockTransplantingPickUpFinished(@Param("name") String name, @Param("loadCar") String loadCar);

    /**
     * 移载卸货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
    @Update({
            "update WCS_MCBlock",
            "set McKey = '',",
            "Command = '35',",
            "Is_Load = 0,",
            "With_Work_Block_Name = '',SC_Block_Name = #{loadCar,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateBlockTransplantingTheUnloadingFinished(@Param("name") String name, @Param("loadCar") String loadCar);

    /**
     * 修改mcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/9 13:47
     */
    @Update({
            "update WCS_MCBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateTwoMcBlock(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

    /**
     * 修改mcKey的值
     *
     * @param name  数据block名称
     * @param mcKey 任务标识
     * @return int
     * @author CalmLake
     * @date 2019/1/16 9:41
     */
    @Update({
            "update WCS_MCBlock",
            "set  McKey = #{mcKey,jdbcType=CHAR} ",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateMcKeyByPrimaryKey(@Param("name") String name, @Param("mcKey") String mcKey);

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
            "update WCS_MCBlock",
            "set Command = #{command,jdbcType=NCHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
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
            "update WCS_MCBlock",
            "set Error_Code = #{errorCode,jdbcType=NCHAR}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateBlockErrorCodeByPrimaryKey(@Param("name") String name, @Param("errorCode") String errorCode);
}