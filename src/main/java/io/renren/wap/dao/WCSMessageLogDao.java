package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * wcs消息记录
 *
 * @author CalmLake
 * @date 2019/1/9 14:31
 */
@Repository("WCSMessageLogDao")
@Mapper
public interface WCSMessageLogDao {
    @Delete({
            "delete from WCS_WCSMessageLog",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Delete({
            "delete from WCS_WCSMessageLog"
    })
    int deleteAll();

    @Insert({
            "insert into WCS_WCSMessageLog (\"PLC_NAME\", \"CREATE_TIME\", \"TYPE\", \"MESSAGE\", \"RESERVED1\", \"RESERVED2\")",
            "values ( #{plcName,jdbcType=CHAR}, ",
            "#{createTime,jdbcType=TIMESTAMP}, #{type,jdbcType=TINYINT}, ",
            "#{message,jdbcType=VARCHAR}, #{reserved1,jdbcType=VARCHAR}, ",
            "#{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsWcsmessagelogEntity record);

    @Select({
            "select",
            "ID, Plc_Name, Create_Time, Type, Message, Reserved1, Reserved2",
            "from WCS_WCSMessageLog",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    @Results(id = "wcsMessageLog",value = {
            @Result(column = "ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "Plc_Name", property = "plcName", jdbcType = JdbcType.CHAR),
            @Result(column = "Create_Time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Type", property = "type", jdbcType = JdbcType.TINYINT),
            @Result(column = "Message", property = "message", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR)
    })
    WcsWcsmessagelogEntity selectByPrimaryKey(Integer id);

    /**
     * 获取倒数100条数据
     *
     * @return java.util.List<com.wap.entity.WcsMessageLog>
     * @author CalmLake
     * @date 2019/2/27 10:04
     */
    @Select({
            "select",
            " *",
            "from WCS_WCSMessageLog where  rownum <= 100",
            "order by ID desc"
    })
    @ResultMap("wcsMessageLog")
    List<WcsWcsmessagelogEntity> getTop100DescList();

    @Update({
            "update WCS_WCSMessageLog",
            "set Plc_Name = #{plcName,jdbcType=CHAR},",
            "Create_Time = #{createTime,jdbcType=TIMESTAMP},",
            "Type = #{type,jdbcType=TINYINT},",
            "Message = #{message,jdbcType=VARCHAR},",
            "Reserved1 = #{reserved1,jdbcType=VARCHAR},",
            "Reserved2 = #{reserved2,jdbcType=VARCHAR}",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WcsWcsmessagelogEntity record);
}