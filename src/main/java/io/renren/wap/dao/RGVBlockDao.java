package io.renren.wap.dao;

import io.renren.modules.generator.entity.WcsRgvblockEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RGV运行信息
 *
 * @author CalmLake
 * @date 2019/1/9 14:27
 */
@Repository("RGVBlockDao")
@Mapper
public interface RGVBlockDao extends BlockDao{
    @Delete({
            "delete from WCS_RGVBlock",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String name);

    @Insert({
            "insert into WCS_RGVBlock (Name, McKey, Appointment_McKey, ",
            "Command, Error_Code, ",
            "Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, Reserved1, ",
            "Reserved2)",
            "values (#{name,jdbcType=CHAR}, #{mcKey,jdbcType=CHAR}, #{appointmentMcKey,jdbcType=CHAR}, ",
            "#{command,jdbcType=NCHAR}, #{errorCode,jdbcType=NCHAR}, ",
            "#{status,jdbcType=NCHAR}, #{isLoad,jdbcType=BIT}, #{withWorkBlockName,jdbcType=CHAR}, ",
            "#{berthBlockName,jdbcType=CHAR}, #{reserved1,jdbcType=VARCHAR}, ",
            "#{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsRgvblockEntity record);

    /**
     * 获取rgv列表
     *
     * @return java.util.List<com.wap.entity.RgvBlock>
     * @author CalmLake
     * @date 2019/2/22 13:52
     */
    @Select({
            "select",
            "Name, McKey, Appointment_McKey, Command, Error_Code, Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, Reserved1, Reserved2",
            "from WCS_RGVBlock"
    })
    @Results(id = "rgvBlock",value = {
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "McKey", property = "mcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Appointment_McKey", property = "appointmentMcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Command", property = "command", jdbcType = JdbcType.NCHAR),
            @Result(column = "Error_Code", property = "errorCode", jdbcType = JdbcType.NCHAR),
            @Result(column = "Status", property = "status", jdbcType = JdbcType.NCHAR),
            @Result(column = "Is_Load", property = "isLoad", jdbcType = JdbcType.BIT),
            @Result(column = "With_Work_Block_Name", property = "withWorkBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Berth_Block_Name", property = "berthBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR)
    })
    List<WcsRgvblockEntity> getRgvBlockList();

    /**
     * RGV：根据数据block名称查询所有字段信息
     *
     * @param name 数据block名称
     * @return com.wap.entity.RgvBlock
     * @author CalmLake
     * @date 2019/1/4 17:28
     */
    @Override
    @Select({
            "select",
            "Name, McKey, Appointment_McKey, Command, Error_Code, Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, Reserved1, Reserved2",
            "from WCS_RGVBlock",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    @ResultMap("rgvBlock")
    WcsRgvblockEntity selectByPrimaryKey(String name);

    @Update({
            "update WCS_RGVBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "Command = #{command,jdbcType=NCHAR},",
            "Error_Code = #{errorCode,jdbcType=NCHAR},",
            "Status = #{status,jdbcType=NCHAR},",
            "Is_Load = #{isLoad,jdbcType=BIT},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR},",
            "Berth_Block_Name = #{berthBlockName,jdbcType=CHAR},",
            "Reserved1 = #{reserved1,jdbcType=VARCHAR},",
            "Reserved2 = #{reserved2,jdbcType=VARCHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsRgvblockEntity record);

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
            "update WCS_RGVBlock",
            "set  Is_Load = #{isLoad,jdbcType=BIT}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
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
            "update WCS_RGVBlock",
            "set Command = #{command,jdbcType=NCHAR},Is_Load = #{isLoad,jdbcType=BIT} ",
            "where trim(Name)= #{name,jdbcType=CHAR}"
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
            "update WCS_RGVBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateThreeRgvBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_RGVBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateTwoRgvBlock(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_RGVBlock",
            "set McKey = #{mcKey,jdbcType=CHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
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
            "update WCS_RGVBlock",
            "set ",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKey(@Param("appointmentMcKey") String appointmentMcKey, @Param("name") String name);

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
            "update WCS_RGVBlock",
            "set Command = #{command,jdbcType=NCHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateCommandByPrimaryKey(@Param("name") String name, @Param("command") String command);

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
            "update WCS_RGVBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateThreeValueBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);
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
            "update WCS_RGVBlock",
            "set Error_Code = #{errorCode,jdbcType=NCHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateBlockErrorCodeByPrimaryKey(@Param("name") String name, @Param("errorCode") String errorCode);

    /**
     * 移动完成修改
     *
     * @param name           数据block名称
     * @param berthBlockName 停泊位置block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:04
     */
    @Update({
            "update WCS_RGVBlock",
            "set ",
            "Command = '35',",
            "Berth_Block_Name = #{berthBlockName,jdbcType=CHAR}",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateMoveFinishByPrimaryKey(@Param("name") String name, @Param("berthBlockName") String berthBlockName);


    /**
     * 移载取货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    @Update({
            "update WCS_RGVBlock",
            "set Command = '35',",
            "Is_Load = 1,",
            "With_Work_Block_Name = ''",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateBlockTransplantingPickUpFinished(@Param("name") String name);

    /**
     * 移载卸货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
    @Update({
            "update WCS_RGVBlock",
            "set McKey = '',",
            "Command = '35',",
            "Is_Load = 0,",
            "With_Work_Block_Name = ''",
            "where trim(Name)= #{name,jdbcType=CHAR}"
    })
    int updateBlockTransplantingTheUnloadingFinished(@Param("name") String name);

}