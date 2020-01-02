package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsClblockEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 输送线运行信息
 *
 * @author CalmLake
 * @date 2019/1/9 14:23
 */
@Repository("CLBlockDao")
@Mapper
public interface CLBlockDao extends BlockDao {


    @Delete({
            "delete from WCS_CLBlock",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String name);

    @Insert({
            "insert into WCS_CLBlock (Name, McKey, Appointment_McKey, ",
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
    int insert(WcsClblockEntity record);

    /**
     * 获取输送线状态列表
     *
     * @return java.util.List<com.wap.entity.ClBlock>
     * @author CalmLake
     * @date 2019/2/22 12:03
     */
    @Select({
            "select",
            "Name, McKey, Appointment_McKey, Command, Error_Code, Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, Reserved1, Reserved2",
            "from WCS_CLBlock"
    })
    @Results(id = "clBlock", value = {
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
    List<WcsClblockEntity> getClBlockList();

    /**
     * 输送线：根据数据block名称查询所有字段信息
     *
     * @param name 数据block名称
     * @return com.wap.entity.ClBlock
     * @author CalmLake
     * @date 2019/1/4 17:28
     */
    @Override
    @Select({
            "select",
            "Name, McKey, Appointment_McKey, Command, Error_Code, Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, Reserved1, Reserved2",
            "from WCS_CLBlock",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    @ResultMap("clBlock")
    WcsClblockEntity selectByPrimaryKey(String name);

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
            "update WCS_CLBlock",
            "set  \"IS_LOAD\"= #{isLoad,jdbcType=BIT}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateLoad(@Param("name") String name, @Param("isLoad") boolean isLoad);

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
            "update WCS_CLBlock",
            "set \"MCKEY\"=#{mcKey,jdbcType=CHAR},",
            "\"APPOINTMENT_MCKEY\"=#{appointmentMcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\"=#{withWorkBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateThreeValueBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);
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
            "update WCS_CLBlock",
            "set \"COMMAND\"= #{command,jdbcType=NCHAR},\"IS_LOAD\"= #{isLoad,jdbcType=BIT} ",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateCommandAndLoad(@Param("name") String name, @Param("command") String command, @Param("isLoad") boolean isLoad);

    /**
     * 修改停泊设备名称
     *
     * @param name           数据block名称
     * @param berthBlockName 停泊设备名称
     * @return int
     * @author CalmLake
     * @date 2019/1/22 17:36
     */
    @Update({
            "update WCS_CLBlock",
            "set  ",
            "\"BERTH_BLOCK_NAME\"= #{berthBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateBerthBlockNameByPrimaryKey(@Param("name") String name, @Param("berthBlockName") String berthBlockName);

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
            "update WCS_CLBlock",
            "set \"MCKEY\"=#{mcKey,jdbcType=CHAR},",
            "\"APPOINTMENT_MCKEY\"=#{appointmentMcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\"=#{withWorkBlockName,jdbcType=CHAR},\"RESERVED1\"= ''",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateThreeCLBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_CLBlock",
            "set \"MCKEY\"=#{mcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\"=#{withWorkBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateTwoCLBlock(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

    /**
     * 修改载荷状态和mcKey
     *
     * @param mcKey  mcKey
     * @param isLoad 载荷状态
     * @param name   数据block编号
     * @return int
     * @author CalmLake
     * @date 2019/4/3 17:08
     */
    @Update({
            "update WCS_CLBlock",
            "set \"MCKEY\"=#{mcKey,jdbcType=CHAR},\"IS_LOAD\"= #{isLoad,jdbcType=BIT} ",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateMcKeyAndLoad(@Param("mcKey") String mcKey, @Param("isLoad") boolean isLoad, @Param("name") String name);

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
            "update WCS_CLBlock",
            "set \"MCKEY\"=#{mcKey,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
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
            "update WCS_CLBlock",
            "set ",
            "\"APPOINTMENT_MCKEY\"=#{appointmentMcKey,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKey(@Param("appointmentMcKey") String appointmentMcKey, @Param("name") String name);

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
            "update WCS_CLBlock",
            "set \"MCKEY\"=#{mcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\"=#{withWorkBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateMcKeyCLBlock(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_CLBlock",
            "set \"MCKEY\"=#{mcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\"=#{withWorkBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateMcKeyByName(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

    /**
     * 修改appointmentMcKey和预约交互设备编号
     *
     * @param appointmentMcKey appointmentMcKey
     * @param name             数据block编号
     * @param reserved1        预约交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/9 13:47
     */
    @Update({
            "update WCS_CLBlock",
            "set \"APPOINTMENT_MCKEY\"=#{appointmentMcKey,jdbcType=CHAR},",
            "\"RESERVED1\"= #{reserved1,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKeyCLBlock(@Param("appointmentMcKey") String appointmentMcKey, @Param("reserved1") String reserved1, @Param("name") String name);

    /**
     * 修改appointmentMcKey和预约交互设备编号
     *
     * @param appointmentMcKey appointmentMcKey
     * @param name             数据block编号
     * @param reserved1        预约交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/9 13:47
     */
    @Override
    @Update({
            "update WCS_CLBlock",
            "set \"APPOINTMENT_MCKEY\"=#{appointmentMcKey,jdbcType=CHAR},",
            "\"RESERVED1\"= #{reserved1,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKeyReserved1ByName(@Param("name") String name, @Param("appointmentMcKey") String appointmentMcKey, @Param("reserved1") String reserved1);

    /**
     * 修改输送线载荷状态
     *
     * @param name block名称
     * @param load 载荷状态
     * @return int
     * @author CalmLake
     * @date 2019/3/1 13:45
     */
    @Update({
            "update WCS_CLBlock",
            "set  ",
            "\"IS_LOAD\"= 1",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateCLBlockLoad(@Param("name") String name, @Param("load") byte load);

    /**
     * 移载取货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    @Update({
            "update WCS_CLBlock",
            "set \"COMMAND\"= '35',",
            "\"IS_LOAD\"= 1,",
            "\"WITH_WORK_BLOCK_NAME\"=''",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateCLBlockTransplantingPickUpFinished(@Param("name") String name);

    /**
     * 移载卸货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
    @Update({
            "update WCS_CLBlock",
            "set \"MCKEY\"='',",
            "\"COMMAND\"= '35',",
            "\"IS_LOAD\"= 0,",
            "\"WITH_WORK_BLOCK_NAME\"='',\"BERTH_BLOCK_NAME\"= '' ",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateCLBlockTransplantingTheUnloadingFinished(@Param("name") String name);

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
            "update WCS_CLBlock",
            "set \"COMMAND\"= #{command,jdbcType=NCHAR} ",
            "where NAME= #{name,jdbcType=CHAR}"
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
            "update WCS_CLBlock",
            "set \"ERROR_CODE\"= #{errorCode,jdbcType=NCHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateBlockErrorCodeByPrimaryKey(@Param("name") String name, @Param("errorCode") String errorCode);

    @Update({
            "update WCS_CLBlock",
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
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsClblockEntity record);

    @Update({
            "update WCS_CLBLOCK",
            "set \"MCKEY\" ='',",
            "\"APPOINTMENT_MCKEY\" = '',",
            "\"COMMAND\" = '05',",
            "\"ERROR_CODE\" = '00',",
            "\"STATUS\" = '1',",
            "\"IS_LOAD\" = '0',",
            "\"WITH_WORK_BLOCK_NAME\" = '',",
            "\"BERTH_BLOCK_NAME\" = '',",
            "\"RESERVED1\"= '',",
            "\"RESERVED2\" = ''",
            " where \"NAME\" IN('0001','0002','0003')"
    })
    int updateOneRecovery();
}