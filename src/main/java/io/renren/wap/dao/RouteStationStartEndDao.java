package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsRoutestationstartendEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 路径起止信息
 *
 * @author CalmLake
 * @date 2019/5/19 13:53
 */
@Repository("RouteStationStartEndDao")
@Mapper
public interface RouteStationStartEndDao {

    /**
     * 根据唯一序号获取起止路径信息
     *
     * @param id 序号
     * @return com.wap.entity.RouteStationStartEnd
     * @author CalmLake
     * @date 2019/5/19 14:13
     */
    @Select({
            "select",
            "Id, From_Station, End_Station, Type",
            "from WCS_RouteStationStartEnd",
            "where Id = #{id,jdbcType=INTEGER}"
    })
    @Results(id = "routeStationStartEnd", value = {
            @Result(column = "Id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "From_Station", property = "fromStation", jdbcType = JdbcType.CHAR),
            @Result(column = "End_Station", property = "endStation", jdbcType = JdbcType.CHAR),
            @Result(column = "Type", property = "type", jdbcType = JdbcType.TINYINT)
    })
    WcsRoutestationstartendEntity selectByPrimaryKey(Integer id);

    /**
     * 获取所有起止信息
     *
     * @return java.util.List<com.wap.entity.RouteStationStartEnd>
     * @author CalmLake
     * @date 2019/5/19 14:12
     */
    @Select({
            "select",
            "Id, From_Station, End_Station, Type",
            "from WCS_RouteStationStartEnd"
    })
    @ResultMap("routeStationStartEnd")
    List<WcsRoutestationstartendEntity> getList();

    /**
     * 统计相同起止站台数
     *
     * @param fromStation 起始站台
     * @param endStation  终止站台
     * @return int
     * @author CalmLake
     * @date 2019/5/19 14:19
     */
    @Select({
            "select",
            "count(*)",
            "from WCS_RouteStationStartEnd",
            "where trim(From_Station) = #{fromStation,jdbcType=CHAR} and trim(End_Station) = #{endStation,jdbcType=CHAR}"
    })
    int countNumByFromStationAndEndStation(@Param("fromStation") String fromStation, @Param("endStation") String endStation);
}