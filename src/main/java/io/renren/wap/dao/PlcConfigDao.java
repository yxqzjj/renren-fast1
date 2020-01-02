package io.renren.wap.dao;

import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * plc配置信息
 *
 * @author CalmLake
 * @date 2019/1/9 14:26
 */
@Repository("PlcConfigDao")
@Mapper
public interface PlcConfigDao {
    @Delete({
            "delete from WCS_PlcConfig",
            "where trim(Name)=#{name,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String name);

    @Insert({
            "insert into WCS_PlcConfig (Name, IP, Port, ",
            "Status, HeartBeat_Time, ",
            "Reserved1)",
            "values (#{name,jdbcType=CHAR}, #{ip,jdbcType=CHAR}, #{port,jdbcType=INTEGER}, ",
            "#{status,jdbcType=TINYINT}, #{heartbeatTime,jdbcType=TIMESTAMP}, ",
            "#{reserved1,jdbcType=VARCHAR})"
    })
    int insert(WcsPlcconfigEntity record);

    @Select({
            "select",
            "Name, IP, Port, Status, HeartBeat_Time, Reserved1",
            "from WCS_PlcConfig"
    })
    @Results(id = "plcConfig", value = {
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "IP", property = "ip", jdbcType = JdbcType.CHAR),
            @Result(column = "Port", property = "port", jdbcType = JdbcType.INTEGER),
            @Result(column = "Status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "HeartBeat_Time", property = "heartbeatTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR)
    })
    List<WcsPlcconfigEntity> getPlcList();

    @Select({
            "select",
            "Name, IP, Port, Status, HeartBeat_Time, Reserved1",
            "from WCS_PlcConfig",
            "where trim(Name)=#{name,jdbcType=CHAR}"
    })
    @ResultMap("plcConfig")
    WcsPlcconfigEntity selectByPrimaryKey(String name);

    @Update({
            "update WCS_PlcConfig",
            "set IP = #{ip,jdbcType=CHAR},",
            "Port = #{port,jdbcType=INTEGER},",
            "Status = #{status,jdbcType=TINYINT},",
            "HeartBeat_Time = #{heartbeatTime,jdbcType=TIMESTAMP},",
            "Reserved1 = #{reserved1,jdbcType=VARCHAR}",
            "where trim(Name)=#{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsPlcconfigEntity
                                   record);

    /**
     * 修改plc连接状态
     *
     * @param status 状态
     * @param name   名称
     * @return int
     * @author CalmLake
     * @date 2019/2/28 14:47
     */
    @Update({
            "update WCS_PlcConfig",
            "set Status = #{status,jdbcType=TINYINT} ",
            "where trim(Name)=#{name,jdbcType=CHAR}"
    })
    int updateStatusByPrimaryKey(@Param("status") byte status, @Param("name") String name);

    /**
     * 根据名称修改心跳时间
     *
     * @param heartbeatTime 时间
     * @param name          名称
     * @return int
     * @author CalmLake
     * @date 2019/1/16 14:54
     */
    @Update({
            "update WCS_PlcConfig",
            "set HeartBeat_Time = #{heartbeatTime,jdbcType=TIMESTAMP} ",
            "where trim(Name)=#{name,jdbcType=CHAR}"
    })
    int updateHeartTimeByPrimaryKey(@Param("heartbeatTime") Date heartbeatTime, @Param("name") String name);

    /**
     * 根据名称修改心跳时间和状态
     *
     * @param status        状态
     * @param heartbeatTime 时间
     * @param name          名称
     * @return int
     * @author CalmLake
     * @date 2019/8/12 9:53
     */
    @Update({
            "update WCS_PlcConfig",
            "set  Status = #{status,jdbcType=TINYINT},HeartBeat_Time = #{heartbeatTime,jdbcType=TIMESTAMP} ",
            "where trim(Name)=#{name,jdbcType=CHAR}"
    })
    int updateHeartTimeAndStatusByPrimaryKey(@Param("status") byte status, @Param("heartbeatTime") Date heartbeatTime, @Param("name") String name);
}