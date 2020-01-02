package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsCommandlogEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 动作指令记录
 *
 * @author CalmLake
 * @date 2019/1/9 14:23
 */
@Repository("CommandLogDao")
@Mapper
public interface CommandLogDao {
    /**
     * 插入消息
     *
     * @param record 消息
     * @return int
     * @author CalmLake
     * @date 2019/2/21 16:37
     */
    @Insert({
            "INSERT INTO \"WCS_COMMANDLOG\"(\"COMMAND\", \"SEQ_NO\", \"CREATE_TIME\", \"BLOCK_NAME\", \"CYCLE_COMMAND\", \"CYCLE_TYPE\", \"MCKEY\", \"STATION\", \"DOCK\", \"TIER\", \"LINE\", \"ROW\", \"LOAD\", \"ACK_TYPE\", \"ERROR_TYPE\", \"FINISH_TYPE\", \"RESEND\", \"RESERVED1\", \"RESERVED2\") ",
            "values (#{command,jdbcType=CHAR}, #{seqNo,jdbcType=CHAR}, ",
            "#{createTime,jdbcType=TIMESTAMP}, #{blockName,jdbcType=CHAR}, ",
            "#{cycleCommand,jdbcType=CHAR}, #{cycleType,jdbcType=CHAR}, ",
            "#{mcKey,jdbcType=CHAR}, #{station,jdbcType=CHAR}, #{dock,jdbcType=CHAR}, ",
            "#{tier,jdbcType=CHAR}, #{line,jdbcType=CHAR}, #{row,jdbcType=CHAR}, ",
            "#{load,jdbcType=CHAR}, #{ackType,jdbcType=CHAR}, #{errorType,jdbcType=CHAR}, ",
            "#{finishType,jdbcType=CHAR}, #{resend,jdbcType=CHAR}, #{reserved1,jdbcType=VARCHAR}, ",
            "#{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsCommandlogEntity record);

    /**
     * 获取所有消息
     *
     * @return java.util.List<com.wap.entity.CommandLog>
     * @author CalmLake
     * @date 2019/2/21 16:09
     */
    @Select({
            "select * from WCS_CommandLog where (command ='03'  or command ='35')   order by ID desc"
    })
    @Results(id = "commandLog", value = {
            @Result(column = "ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "Command", property = "command", jdbcType = JdbcType.CHAR),
            @Result(column = "Seq_No", property = "seqNo", jdbcType = JdbcType.CHAR),
            @Result(column = "Create_Time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Block_Name", property = "blockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Cycle_Command", property = "cycleCommand", jdbcType = JdbcType.CHAR),
            @Result(column = "Cycle_Type", property = "cycleType", jdbcType = JdbcType.CHAR),
            @Result(column = "McKey", property = "mcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Station", property = "station", jdbcType = JdbcType.CHAR),
            @Result(column = "Dock", property = "dock", jdbcType = JdbcType.CHAR),
            @Result(column = "Tier", property = "tier", jdbcType = JdbcType.CHAR),
            @Result(column = "Line", property = "line", jdbcType = JdbcType.CHAR),
            @Result(column = "Row", property = "row", jdbcType = JdbcType.CHAR),
            @Result(column = "Load", property = "load", jdbcType = JdbcType.CHAR),
            @Result(column = "Ack_Type", property = "ackType", jdbcType = JdbcType.CHAR),
            @Result(column = "Error_Type", property = "errorType", jdbcType = JdbcType.CHAR),
            @Result(column = "Finish_Type", property = "finishType", jdbcType = JdbcType.CHAR),
            @Result(column = "Resend", property = "resend", jdbcType = JdbcType.CHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR)
    })
    List<WcsCommandlogEntity> getList();

    /**
     * 根据id进行查找消息命令
     *
     * @param id 序号主键
     * @return com.wap.entity.CommandLog
     * @author CalmLake
     * @date 2019/2/22 9:57
     */
    @Select({
            "select * from WCS_CommandLog where ID= #{id,jdbcType=INTEGER}"
    })
    @ResultMap("commandLog")
    WcsCommandlogEntity getCommandLogById(@Param("id") int id);

    /**
     * 通过任务标识查找
     *
     * @param mcKey 任务标识
     * @return java.util.List<com.wap.entity.CommandLog>
     * @author CalmLake
     * @date 2019/6/14 10:09
     */
    @Select({
            "select * from WCS_CommandLog where (command ='03'  or command ='35')   and trim(McKey) = #{mcKey,jdbcType=CHAR} order by ID desc"
    })
    @ResultMap("commandLog")
    List<WcsCommandlogEntity> selectByMcKey(@Param("mcKey") String mcKey);

    /**
     * 通过设备名称查找
     *
     * @param blockName 任务标识
     * @return java.util.List<com.wap.entity.CommandLog>
     * @author CalmLake
     * @date 2019/6/14 10:09
     */
    @Select({
            "select * from WCS_CommandLog where (command ='03'  or command ='35')   and trim(Block_Name)  = #{blockName,jdbcType=CHAR} order by ID desc"
    })
    @ResultMap("commandLog")
    List<WcsCommandlogEntity> selectByBlockName(@Param("blockName") String blockName);


    /**
     * 通过任务标识设备名称查找
     *
     * @param blockName 任务标识
     * @return java.util.List<com.wap.entity.CommandLog>
     * @author CalmLake
     * @date 2019/6/14 10:09
     */
    @Select({
            "select * from WCS_CommandLog where  (command ='03'  or command ='35')   and trim(McKey)  = #{mcKey,jdbcType=CHAR} and trim(Block_Name)   = #{blockName,jdbcType=CHAR} order by ID desc"
    })
    @ResultMap("commandLog")
    List<WcsCommandlogEntity> selectByMcKeyBlockName(@Param("mcKey") String mcKey, @Param("blockName") String blockName);

    /**
     * 删除指定mvkey的消息记录
     *
     * @param mcKey 工作计划标识
     * @return int
     * @author CalmLake
     * @date 2019/3/4 16:57
     */
    @Delete({
            "delete WCS_CommandLog where  trim(McKey)   = #{mcKey,jdbcType=CHAR}"
    })
    int deleteByMcKey(@Param("mcKey") String mcKey);

    @Delete({
            "delete WCS_CommandLog"
    })
    int deleteAll();
}