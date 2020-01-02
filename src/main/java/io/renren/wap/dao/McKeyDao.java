package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsMckeyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 搬送货位唯一标识
 * @author CalmLake
 * @date 2019/1/7 17:07
 */
@Repository("McKeyDao")
@Mapper
public interface McKeyDao {

    /**
     * 查找最后一次使用的McKey的值
     * @author CalmLake
     * @date 2019/1/7 17:08
     * @return com.wap.entity.McKey
     */
    @Select({"select    * from WCS_McKey where  rownum <= 1"})
    WcsMckeyEntity select();

    /**
     * 修改McKey为当前使用的值
     * @author CalmLake
     * @date 2019/1/7 17:09
     * @param mcKey 值 0000-9999
     * @return int
     */
    @Update({"update WCS_McKey set McKey = #{mcKey,jdbcType=CHAR} "})
    int update(WcsMckeyEntity mcKey);
}