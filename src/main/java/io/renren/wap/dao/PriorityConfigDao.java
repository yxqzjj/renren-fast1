package io.renren.wap.dao;

import io.renren.modules.generator.entity.WcsPriorityconfigEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工作计划优先级信息
 *
 * @author CalmLake
 * @date 2019/3/8 14:33
 */
@Repository("PriorityConfigDao")
@Mapper
public interface PriorityConfigDao {
    @Delete({
            "delete from WCS_PriorityConfig",
            "where WorkPlanType = #{workPlanType,jdbcType=TINYINT}"
    })
    int deleteByPrimaryKey(Byte workPlanType);

    @Insert({
            "insert into WCS_PriorityConfig (WorkPlanType, Priority)",
            "values (#{workPlanType,jdbcType=TINYINT}, #{priority,jdbcType=TINYINT})"
    })
    int insert(WcsPriorityconfigEntity record);

    /**
     * 获取所有信息
     *
     * @return java.util.List<com.wap.entity.PriorityConfig>
     * @author CalmLake
     * @date 2019/3/8 14:35
     */
    @Select({
            "select",
            "WorkPlanType, Priority",
            "from WCS_PriorityConfig"
    })
    @Results({
            @Result(column = "WorkPlanType", property = "workPlanType", jdbcType = JdbcType.TINYINT, id = true),
            @Result(column = "Priority", property = "priority", jdbcType = JdbcType.TINYINT)
    })
    List<WcsPriorityconfigEntity> getList();

    @Select({
            "select",
            "WorkPlanType, Priority",
            "from WCS_PriorityConfig",
            "where WorkPlanType = #{workPlanType,jdbcType=TINYINT}"
    })
    @Results({
            @Result(column = "WorkPlanType", property = "workPlanType", jdbcType = JdbcType.TINYINT, id = true),
            @Result(column = "Priority", property = "priority", jdbcType = JdbcType.TINYINT)
    })
    WcsPriorityconfigEntity selectByPrimaryKey(Byte workPlanType);

    @Update({
            "update WCS_PriorityConfig",
            "set Priority = #{priority,jdbcType=TINYINT}",
            "where WorkPlanType = #{workPlanType,jdbcType=TINYINT}"
    })
    int updateByPrimaryKey(WcsPriorityconfigEntity record);
}