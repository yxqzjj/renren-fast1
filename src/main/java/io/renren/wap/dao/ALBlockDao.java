package io.renren.wap.dao;

import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.wap.dao.provider.ALBlockProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 升降机运行信息
 *
 * @Author: CalmLake
 * @Date: 2019/1/4  16:05
 * @Version: V1.0.0
 **/
@Repository("ALBlockDao")
@Mapper
public interface ALBlockDao extends BlockDao {
    /**
     * 获取升降机列表
     *
     * @return java.util.List<com.wap.entity.WcsAlblockEntity>
     * @author CalmLake
     * @date 2019/2/22 13:50
     */
    @Select({
            "select",
            "Name, McKey, Appointment_McKey, Command, Error_Code, Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, Tier, Reserved1, Reserved2",
            "from WCS_ALBlock"
    })
    @Results(id = "alBlock", value = {
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "McKey", property = "mcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Appointment_McKey", property = "appointmentMcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Command", property = "command", jdbcType = JdbcType.NCHAR),
            @Result(column = "Error_Code", property = "errorCode", jdbcType = JdbcType.NCHAR),
            @Result(column = "Status", property = "status", jdbcType = JdbcType.NCHAR),
            @Result(column = "Is_Load", property = "isLoad", jdbcType = JdbcType.BIT),
            @Result(column = "With_Work_Block_Name", property = "withWorkBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Berth_Block_Name", property = "berthBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Tier", property = "tier", jdbcType = JdbcType.CHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR)
    })
    List<WcsAlblockEntity> getAlblockList();

    /**
     * 升降机：根据数据block名称查询所有字段信息
     *
     * @param name 数据block名称
     * @return com.wap.entity.WcsAlblockEntity
     * @author CalmLake
     * @date 2019/1/4 17:28
     */
    @Override
    @Select({
            "select",
            "Name, McKey, Appointment_McKey, Command, Error_Code, Status, Is_Load, With_Work_Block_Name, ",
            "Berth_Block_Name, Tier, Reserved1, Reserved2",
            "from WCS_ALBlock",
            "where Name = #{name,jdbcType=CHAR}"
    })
    @ResultMap("alBlock")
    WcsAlblockEntity selectByPrimaryKey(String name);

    @Update({
            "update WCS_ALBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "Command = #{command,jdbcType=NCHAR},",
            "Error_Code = #{errorCode,jdbcType=NCHAR},",
            "Status = #{status,jdbcType=NCHAR},",
            "Is_Load = #{isLoad,jdbcType=BIT},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR},",
            "Berth_Block_Name = #{berthBlockName,jdbcType=CHAR},",
            "Tier = #{tier,jdbcType=CHAR},",
            "Reserved1 = #{reserved1,jdbcType=VARCHAR},",
            "Reserved2 = #{reserved2,jdbcType=VARCHAR}",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsAlblockEntity record);

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
            "update WCS_ALBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where Name = #{name,jdbcType=CHAR}"
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
            "update WCS_ALBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where Name = #{name,jdbcType=CHAR}"
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
            "update WCS_ALBlock",
            "set  Appointment_McKey =  #{appointmentMcKey,jdbcType=CHAR},Reserved1=#{reserved1,jdbcType=VARCHAR}",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKeyReserved1ByName(@Param("name") String name, @Param("appointmentMcKey") String appointmentMcKey, @Param("reserved1") String reserved1);

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
            "update WCS_ALBlock",
            "set  Is_Load = #{isLoad,jdbcType=BIT}",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateLoad(@Param("name") String name, @Param("isLoad") boolean isLoad);

    /*
        */
/**
     * 根据主键修改升降机对象数据
     *
     * @param alBlock 升降机
     * @return int
     * @author CalmLake
     * @date 2019/3/5 17:31
     */    /*

    @UpdateProvider(type = ALBlockProvider.class, method = "updateALBlockProvider")
    int updateALBlockProvider(WcsAlblockEntity alBlock);
        */
/**
     * 根据主键修改升降机对象数据
     *
     * @param alBlock 升降机
     * @return int
     * @author CalmLake
     * @date 2019/3/5 17:31
     */    /*

    @UpdateProvider(type = ALBlockProvider.class, method = "updateALBlockProvider")
    int updateALBlockProvider(WcsAlblockEntity alBlock);
    */

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
            "update WCS_ALBlock",
            "set Command = #{command,jdbcType=NCHAR},Is_Load = #{isLoad,jdbcType=BIT} ",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateCommandAndLoad(@Param("name") String name, @Param("command") String command, @Param("isLoad") boolean isLoad);

    /**
     * 移动完成修改
     *
     * @param name           数据block名称
     * @param tier           层
     * @param berthBlockName 停泊位置block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:04
     */
    @Update({
            "update WCS_ALBlock",
            "set ",
            "Command = '35',",
            "Tier = #{tier,jdbcType=CHAR},",
            "Berth_Block_Name = #{berthBlockName,jdbcType=CHAR}",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateMoveFinishByPrimaryKey(@Param("name") String name, @Param("tier") String tier, @Param("berthBlockName") String berthBlockName);


    /**
     * 移载取货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    @Update({
            "update WCS_ALBlock",
            "set Command = '35',",
            "Is_Load = 1,",
            "With_Work_Block_Name = ''",
            "where Name = #{name,jdbcType=CHAR}"
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
            "update WCS_ALBlock",
            "set McKey = '',",
            "Command = '35',",
            "Is_Load = 0,",
            "With_Work_Block_Name = ''",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateBlockTransplantingTheUnloadingFinished(@Param("name") String name);

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
            "update WCS_ALBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateThreeALBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_ALBlock",
            "set McKey = #{mcKey,jdbcType=CHAR},",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateTwoALBlock(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_ALBlock",
            "set McKey = #{mcKey,jdbcType=CHAR}",
            "where Name = #{name,jdbcType=CHAR}"
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
            "update WCS_ALBlock",
            "set ",
            "Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR}",
            "where Name = #{name,jdbcType=CHAR}"
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
            "update WCS_ALBlock",
            "set Command = #{command,jdbcType=NCHAR}",
            "where Name = #{name,jdbcType=CHAR}"
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
            "update WCS_ALBlock",
            "set Error_Code = #{errorCode,jdbcType=NCHAR}",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateBlockErrorCodeByPrimaryKey(@Param("name") String name, @Param("errorCode") String errorCode);

    /**
     * 升降机卸车完成
     *
     * @param name              数据block名称
     * @param scBlockName       子车block名称
     * @param mcKey             任务标识
     * @param withWorkBlockName 一起工作设备名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    @Update({
            "update WCS_ALBlock",
            "set McKey= #{mcKey,jdbcType=CHAR},",
            "Command = '35',",
            "With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR},",
            "Reserved2 = #{scBlockName,jdbcType=VARCHAR} ",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateOffCarFinishByPrimaryKey(@Param("name") String name, @Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("scBlockName") String scBlockName);

    /**
     * 升降机接车完成
     *
     * @param name        数据block名称
     * @param scBlockName 子车block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    @Update({
            "update WCS_ALBlock",
            "set  ",
            "Command = '35',",
            "Reserved2 = #{scBlockName,jdbcType=VARCHAR} ",
            "where Name = #{name,jdbcType=CHAR}"
    })
    int updateGetCarFinishByPrimaryKey(@Param("name") String name, @Param("scBlockName") String scBlockName);

}