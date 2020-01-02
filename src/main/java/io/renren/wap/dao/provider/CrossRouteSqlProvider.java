package io.renren.wap.dao.provider;


import io.renren.modules.generator.entity.WcsCrossrouteEntity;
import org.apache.ibatis.jdbc.SQL;

public class CrossRouteSqlProvider {

    public String updateByPrimaryKeySelective(WcsCrossrouteEntity crossRoute) {
        SQL sql = new SQL();
        sql.UPDATE("WCS_CrossRoute");
        if (crossRoute.getMode() != null) {
            sql.SET("Mode = #{mode,jdbcType=TINYINT}");
        }
        if (crossRoute.getMaxLoadNum() != null) {
            sql.SET("Max_Load_Num = #{maxLoadNum,jdbcType=INTEGER}");
        }
        if (crossRoute.getLoadNum() != null) {
            sql.SET("Load_Num = #{loadNum,jdbcType=INTEGER}");
        }
        sql.WHERE("Run_Block_Name = #{runBlockName,jdbcType=CHAR}");
        return sql.toString();
    }
}