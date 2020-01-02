package io.renren.wap.dao;

import io.renren.modules.generator.entity.WcsErrorcodeEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * 故障信息
 *
 * @author CalmLake
 * @date 2019/6/20 12:02
 */
@Repository("ErrorCodeDao")
@Mapper
public interface ErrorCodeDao {
    @Delete({
            "delete from WCS_ErrorCode",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into WCS_ErrorCode (ID, Type, ",
            "Error_Code, Error_Detail)",
            "values (#{id,jdbcType=INTEGER}, #{type,jdbcType=TINYINT}, ",
            "#{errorCode,jdbcType=VARCHAR}, #{errorDetail,jdbcType=NVARCHAR})"
    })
    int insert(WcsErrorcodeEntity errorCode);

    @Select({
            "select",
            "ID, Type, Error_Code, Error_Detail",
            "from WCS_ErrorCode",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    @Results(id = "errorCode", value = {
            @Result(column = "ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "Type", property = "type", jdbcType = JdbcType.TINYINT),
            @Result(column = "Error_Code", property = "errorCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Error_Detail", property = "errorDetail", jdbcType = JdbcType.NVARCHAR)
    })
    WcsErrorcodeEntity selectByPrimaryKey(Integer id);

    /**
     * 查询故障信息
     *
     * @param type      设备类型
     * @param errorCode 故障代码
     * @return com.wap.entity.ErrorCode
     * @author CalmLake
     * @date 2019/6/20 12:10
     */
    @Select({
            "select",
            " * ",
            "from WCS_ErrorCode",
            "where Type = #{type,jdbcType=TINYINT} and trim(Error_Code)  = #{errorCode,jdbcType=VARCHAR}"
    })
    @ResultMap("errorCode")
    WcsErrorcodeEntity getErrorCodeByTypeAndErrorCode(@Param("type") Byte type, @Param("errorCode") String errorCode);

    @Update({
            "update WCS_ErrorCode",
            "set Type = #{type,jdbcType=TINYINT},",
            "Error_Code = #{errorCode,jdbcType=VARCHAR},",
            "Error_Detail = #{errorDetail,jdbcType=NVARCHAR}",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WcsErrorcodeEntity errorCode);
}