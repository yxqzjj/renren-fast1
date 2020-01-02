package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsRouteEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * 路径信息
 *
 * @author CalmLake
 * @date 2019/1/9 14:27
 */
@Repository("RouteDao")
@Mapper
public interface RouteDao {
    @Insert({
            "insert into WCS_Route (Block_Name, Next_Block_Name, ",
            "To_Station, Status, ",
            "Reserved1, Reserved2)",
            "values (#{blockName,jdbcType=CHAR}, #{nextBlockName,jdbcType=CHAR}, ",
            "#{toStation,jdbcType=CHAR}, #{status,jdbcType=TINYINT}, ",
            "#{reserved1,jdbcType=VARCHAR}, #{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsRouteEntity record);

    /**
     * 根据数据block名称和到达设备查找路径信息
     *
     * @param blockName 数据block编号
     * @param toStation 站台编号
     * @return org.apache.logging.log4j.core.appender.routing.Route
     * @author CalmLake
     * @date 2019/1/9 11:43
     */
    @Select({"select ",
            "Block_Name, Next_Block_Name,To_Station, Status,Reserved1, Reserved2 ",
            "from WCS_Route",
            "where Block_Name = #{blockName,jdbcType=CHAR} and To_Station = #{toStation,jdbcType=CHAR}"})
    @Results(id = "route",value = {
            @Result(column = "Block_Name", property = "blockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Next_Block_Name", property = "nextBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "To_Station", property = "toStation", jdbcType = JdbcType.CHAR),
            @Result(column = "Status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR)
    })
    WcsRouteEntity selectByBlockName(@Param("blockName") String blockName, @Param("toStation") String toStation);

    /**
     * 根据下一数据block名称和到达设备查找路径信息
     *
     * @param nextBlockName 下一数据block名称
     * @param toStation 站台名称
     * @return org.apache.logging.log4j.core.appender.routing.Route
     * @author CalmLake
     * @date 2019/1/9 11:43
     */
    @Select({"select ",
            "Block_Name, Next_Block_Name,To_Station, Status,Reserved1, Reserved2 ",
            "from WCS_Route",
            "where trim(Next_Block_Name) = #{nextBlockName,jdbcType=CHAR} and trim(To_Station) = #{toStation,jdbcType=CHAR}"})
    @ResultMap("route")
    WcsRouteEntity selectByNextBlockName(String nextBlockName, String toStation);
}