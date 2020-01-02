package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsCommandlogEntity;
import io.renren.modules.generator.entity.WcsCrossrouteEntity;
import io.renren.wap.dao.provider.CrossRouteSqlProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 交叉路径信息
 *
 * @author CalmLake
 * @date 2019/3/11 15:58
 */
@Repository("CrossRouteDao")
@Mapper
public interface CrossRouteDao {
    @Delete({
            "delete from WCS_CrossRoute",
            "where trim(Run_Block_Name)  = #{runBlockName,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String runBlockName);

    @Insert({
            "insert into WCS_CrossRoute (Run_Block_Name, Mode, ",
            "Max_Load_Num, Load_Num)",
            "values (#{runBlockName,jdbcType=CHAR}, #{mode,jdbcType=TINYINT}, ",
            "#{maxLoadNum,jdbcType=INTEGER}, #{loadNum,jdbcType=INTEGER})"
    })
    int insert(WcsCommandlogEntity record);

    @Select({
            "select",
            "Run_Block_Name, Mode, Max_Load_Num, Load_Num",
            "from WCS_CrossRoute",
            "where trim(Run_Block_Name) = #{runBlockName,jdbcType=CHAR}"
    })
    @Results(id = "crossRouteMao", value = {
            @Result(column = "Run_Block_Name", property = "runBlockName", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "Mode", property = "mode", jdbcType = JdbcType.TINYINT),
            @Result(column = "Max_Load_Num", property = "maxLoadNum", jdbcType = JdbcType.INTEGER),
            @Result(column = "Load_Num", property = "loadNum", jdbcType = JdbcType.INTEGER)
    })
    WcsCrossrouteEntity selectByPrimaryKey(String runBlockName);

    @Select({
            "select",
            "Run_Block_Name, 'Mode', Max_Load_Num, Load_Num",
            "from WCS_CrossRoute"
    })
    @ResultMap("crossRouteMao")
    List<WcsCrossrouteEntity> getListAll();

    /**
     * 修改交叉路径信息
     *
     * @param crossRoute Run_Block_Name-必须有值
     * @return int
     * @author CalmLake
     * @date 2019/3/11 16:05
     */
    @UpdateProvider(type = CrossRouteSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(WcsCrossrouteEntity crossRoute);

    @Update({
            "update WCS_CrossRoute",
            "set Mode = #{mode,jdbcType=TINYINT},",
            "Max_Load_Num = #{maxLoadNum,jdbcType=INTEGER},",
            "Load_Num = #{loadNum,jdbcType=INTEGER}",
            "where trim(Run_Block_Name) = #{runBlockName,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsCrossrouteEntity record);
}