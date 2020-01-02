package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsMlblockEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 堆垛机运行信息
 *
 * @author CalmLake
 * @date 2019/1/9 14:25
 */
@Repository("MLBlockDao")
@Mapper
public interface MLBlockDao extends BlockDao {
    @Delete({
            "delete from WCS_MLBlock",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String name);

    /**
     * 获取堆垛机block列表
     *
     * @return java.util.List<com.wap.entity.MlBlock>
     * @author CalmLake
     * @date 2019/2/22 13:45
     */
    @Select({
            "select",
            " * ",
            "from WCS_MLBlock"
    })
    @Results(id = "mlBlock", value = {
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
            @Result(column = "Is_Move", property = "isMove", jdbcType = JdbcType.BIT),
            @Result(column = "Row", property = "row", jdbcType = JdbcType.CHAR),
            @Result(column = "Line", property = "line", jdbcType = JdbcType.CHAR),
            @Result(column = "Tier", property = "tier", jdbcType = JdbcType.CHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Is_Standby_Car", property = "isStandbyCar", jdbcType = JdbcType.BIT),
            @Result(column = "Standby_Car_Block_Name", property = "standbyCarBlockName", jdbcType = JdbcType.CHAR)
    })
    List<WcsMlblockEntity> getMlBlockList();

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
            "from WCS_MLBlock"
    })
    @Results(value = {
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true)
    })
    List<String> getMlBlockNameList();

    /**
     * 使用备车的堆垛机名称
     *
     * @param isStandbyCar        是否使用备车
     * @param standbyCarBlockName 备车名称
     * @return com.wap.entity.MlBlock
     * @author CalmLake
     * @date 2019/4/18 11:56
     */
    @Select({
            "select",
            " * from WCS_MLBlock ",
            "where Is_Standby_Car = #{isStandbyCar,jdbcType=BIT} and  Standby_Car_Block_Name = #{standbyCarBlockName,jdbcType=CHAR} "
    })
    @ResultMap("mlBlock")
    WcsMlblockEntity selectMLBlockByIsStandbyCarAndStandbyCarBlockName(@Param("isStandbyCar") boolean isStandbyCar, @Param("standbyCarBlockName") String standbyCarBlockName);

    /**
     * 根据绑定穿梭车设备名称查找堆垛机信息（堆垛机绑定子车设备具有唯一性）
     *
     * @param bingScBlockName 绑定的穿梭子车数据block名称
     * @return com.wap.entity.MlBlock
     * @author CalmLake
     * @date 2019/4/18 11:35
     */
    @Select({
            "select",
            " * from WCS_MLBlock ",
            "where Bing_SC_Block_Name = #{bingScBlockName,jdbcType=CHAR}"
    })
    @ResultMap("mlBlock")
    WcsMlblockEntity selectByBingScBlockName(@Param("bingScBlockName") String bingScBlockName);

    /**
     * 堆垛机：根据数据block名称查询所有字段信息
     *
     * @param name 数据block名称
     * @return com.wap.entity.MlBlock
     * @author CalmLake
     * @date 2019/1/4 17:28
     */
    @Override
    @Select({
            "select",
            " * from WCS_MLBlock ",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    @ResultMap("mlBlock")
    WcsMlblockEntity selectByPrimaryKey(String name);

    /**
     * 更改堆垛机使用备车状态及备车名称
     *
     * @param name                名称
     * @param isStandbyCar        是否使用备车
     * @param standbyCarBlockName 备车名称
     * @return int
     * @author CalmLake
     * @date 2019/4/18 12:05
     */
    @Update({
            "update WCS_MLBlock",
            "set  \"IS_STANDBY_CAR\" = #{isStandbyCar,jdbcType=BIT}, \"STANDBY_CAR_BLOCK_NAME\" = #{standbyCarBlockName,jdbcType=CHAR} ",
            "where NAME = #{name,jdbcType=CHAR}"
    })
    int updateIsStandbyCarByName(@Param("name") String name, @Param("isStandbyCar") boolean isStandbyCar, @Param("standbyCarBlockName") String standbyCarBlockName);

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
            "update WCS_MLBlock",
            "set  \"APPOINTMENT_MCKEY\" =  #{appointmentMcKey,jdbcType=CHAR},\"RESERVED1\" =#{reserved1,jdbcType=VARCHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
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
            "update WCS_MLBlock",
            "set  \"SC_BLOCK_NAME\" = #{scBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
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
            "update WCS_MLBlock",
            "set  \"IS_LOAD\" = #{isLoad,jdbcType=BIT}",
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
            "update WCS_MLBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"APPOINTMENT_MCKEY\" = #{appointmentMcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\" = = #{withWorkBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateThreeValueBlock(@Param("mcKey") String mcKey, @Param("appointmentMcKey") String appointmentMcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_MLBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"APPOINTMENT_MCKEY\" = #{appointmentMcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\" = #{withWorkBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
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
    @Update({
            "update WCS_MLBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\" =  #{withWorkBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateTwoValueMLBlock(@Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("name") String name);

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
            "update WCS_MLBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"WITH_WORK_BLOCK_NAME\" =  #{withWorkBlockName,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
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
            "update WCS_MLBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR}",
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
            "update WCS_MLBlock",
            "set ",
            "\"APPOINTMENT_MCKEY\" = #{appointmentMcKey,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateAppointmentMcKey(@Param("appointmentMcKey") String appointmentMcKey, @Param("name") String name);

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
            "update WCS_MLBlock",
            "set \"COMMAND\" = #{command,jdbcType=NCHAR},\"IS_LOAD\" = #{isLoad,jdbcType=BIT} ",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateCommandAndLoad(@Param("name") String name, @Param("command") String command, @Param("isLoad") boolean isLoad);

    /**
     * 修改command mcKey
     *
     * @param name    blockName
     * @param command 命令种类
     * @param mcKey   任务标识
     * @return int
     * @author CalmLake
     * @date 2019/3/7 17:00
     */
    @Update({
            "update WCS_MLBlock",
            "set \"COMMAND\" = #{command,jdbcType=NCHAR},\"MCKEY\" = #{mcKey,jdbcType=CHAR} ",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateCommandAndMcKey(@Param("name") String name, @Param("command") String command, @Param("mcKey") String mcKey);

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
            "update WCS_MLBlock",
            "set \"COMMAND\" = #{command,jdbcType=NCHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateCommandByPrimaryKey(@Param("name") String name, @Param("command") String command);

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
            "update WCS_MLBlock",
            "set  \"MCKEY\" = #{mcKey,jdbcType=CHAR} ",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateMcKeyByPrimaryKey(@Param("name") String name, @Param("mcKey") String mcKey);

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
            "update WCS_MLBlock",
            "set \"ERROR_CODE\" = #{errorCode,jdbcType=NCHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateBlockErrorCodeByPrimaryKey(@Param("name") String name, @Param("errorCode") String errorCode);

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
            "update WCS_MLBlock",
            "set ",
            "\"COMMAND\" = '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"SC_BLOCK_NAME\" = '',\"IS_MOVE\" =0",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateOffCarFinish2ByPrimaryKey(@Param("name") String name, @Param("isLoad") byte isLoad);

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
            "update WCS_MLBlock",
            "set McKey= #{mcKey,jdbcType=CHAR},",
            "\"COMMAND\" = '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"WITH_WORK_BLOCK_NAME\" =  #{withWorkBlockName,jdbcType=CHAR},",
            "\"SC_BLOCK_NAME\" = #{scBlockName,jdbcType=CHAR},\"IS_MOVE\" =0",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateOffCarFinishByPrimaryKey(@Param("name") String name, @Param("mcKey") String mcKey, @Param("withWorkBlockName") String withWorkBlockName, @Param("scBlockName") String scBlockName, @Param("isLoad") byte isLoad);

    /**
     * 堆垛机接车完成
     *
     * @param name        数据block名称
     * @param scBlockName 子车block名称
     * @param isLoad      载货标识
     *                    Is_Move =0 (堆垛机二次移动标识)
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    @Update({
            "update WCS_MLBlock",
            "set  ",
            "\"COMMAND\" = '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"SC_BLOCK_NAME\" = #{scBlockName,jdbcType=CHAR},\"IS_MOVE\" =0",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateGetCarFinishByPrimaryKey(@Param("name") String name, @Param("scBlockName") String scBlockName, @Param("isLoad") byte isLoad);

    /**
     * 移动完成修改
     *
     * @param name           数据block名称
     * @param berthBlockName 停泊位置block名称
     * @param row            排
     * @param line           列
     * @param tier           层
     *                       Is_Move =1 (堆垛机二次移动标识)
     * @param loadStorage    托盘载荷
     * @param loadCar        子车载荷
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:04
     */
    @Update({
            " UPDATE WCS_MLBLOCK  SET  " +
            "\"COMMAND\" = '35',",
            "\"BERTH_BLOCK_NAME\" = #{berthBlockName,jdbcType=CHAR},",
            "\"ROW\" = #{row,jdbcType=CHAR},",
            "\"LINE\" = #{line,jdbcType=CHAR},\"IS_MOVE\" =1,",
            "\"TIER\" = #{tier,jdbcType=CHAR},\"IS_LOAD\" = #{loadStorage,jdbcType=BIT},\"SC_BLOCK_NAME\" = #{loadCar,jdbcType=CHAR}",
            "where \"NAME\" = #{name,jdbcType=CHAR}"
    })
    int updateMoveFinishByPrimaryKey(@Param("name") String name, @Param("berthBlockName") String berthBlockName, @Param("row") String row, @Param("line") String line, @Param("tier") String tier, @Param("loadStorage") byte loadStorage, @Param("loadCar") String loadCar);

    /**
     * 移载取货完成修改block表
     *
     * @param name    数据block名称
     * @param loadCar 子车载荷
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    @Update({
            "update WCS_MLBlock",
            "set \"COMMAND\" = '35',",
            "\"IS_LOAD\" = 1,",
            "\"WITH_WORK_BLOCK_NAME\" =  '',\"SC_BLOCK_NAME\" = #{loadCar,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateBlockTransplantingPickUpFinished(@Param("name") String name, @Param("loadCar") String loadCar);

    /**
     * 移载卸货完成修改block表
     *
     * @param name    数据block名称
     * @param loadCar 子车载荷
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
    @Update({
            "update WCS_MLBlock",
            "set \"MCKEY\" = '',",
            "\"COMMAND\" = '35',",
            "\"IS_LOAD\" = 0,",
            "\"WITH_WORK_BLOCK_NAME\" =  '',\"SC_BLOCK_NAME\" = #{loadCar,jdbcType=CHAR}",
            "where NAME= #{name,jdbcType=CHAR}"
    })
    int updateBlockTransplantingTheUnloadingFinished(@Param("name") String name, @Param("loadCar") String loadCar);

    @Update({
            "update WCS_MLBlock",
            "set \"MCKEY\" = #{mcKey,jdbcType=CHAR},",
            "\"APPOINTMENT_MCKEY\" = #{appointmentMcKey,jdbcType=CHAR},",
            "\"COMMAND\" = #{command,jdbcType=NCHAR},",
            "\"ERROR_CODE\" = #{errorCode,jdbcType=NCHAR},",
            "\"STATUS\" = #{status,jdbcType=NCHAR},",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"WITH_WORK_BLOCK_NAME\" = #{withWorkBlockName,jdbcType=CHAR},",
            "\"BERTH_BLOCK_NAME\"= #{berthBlockName,jdbcType=CHAR},",
            "\"SC_BLOCK_NAME\" = #{scBlockName,jdbcType=CHAR},",
            "\"BING_SC_BLOCK_NAME\" = #{bingScBlockName,jdbcType=CHAR},",
            "\"IS_MOVE\" = #{isMove,jdbcType=BIT},",
            "\"ROW = #{row,jdbcType=CHAR},",
            "\"LINE\" = #{line,jdbcType=CHAR},",
            "\"TIER\" = #{tier,jdbcType=CHAR},",
            "\"RESERVED1\" = #{reserved1,jdbcType=VARCHAR},",
            "\"RESERVED2\" = #{reserved2,jdbcType=VARCHAR}",
            "where \"NAME\"= #{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsMlblockEntity record);
    @Update({
            "update WCS_MLBlock",
            "set \"MCKEY\" = '',",
            "\"APPOINTMENT_MCKEY\" = '',",
            "\"COMMAND\" = '05',",
            "\"ERROR_CODE\" = '00',",
            "\"STATUS\" = '1',",
            "\"IS_LOAD\" = '0',",
            "\"WITH_WORK_BLOCK_NAME\" = '',",
            "\"BERTH_BLOCK_NAME\"= '0000',",
            "\"SC_BLOCK_NAME\" = 'SC01',",
            "\"BING_SC_BLOCK_NAME\" = 'SC01',",
            "\"IS_MOVE\" = '0',",
            "\"ROW\"= '00',",
            "\"LINE\" = '00',",
            "\"TIER\" = '00',",
            "\"RESERVED1\" = '',",
            "\"RESERVED2\" = ''",
            " where \"NAME\"= 'ML01'"
    })
    int updateOneRecovery();
}