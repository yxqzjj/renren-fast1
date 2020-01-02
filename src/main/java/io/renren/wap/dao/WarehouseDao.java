package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsWarehouseEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 库区信息
 *
 * @author CalmLake
 * @date 2019/6/21 14:31
 */
@Repository("WarehouseDao")
@Mapper
public interface WarehouseDao {
    @Delete({
            "delete from WCS_Warehouse",
            "where ID = #{id,jdbcType=SMALLINT}"
    })
    int deleteByPrimaryKey(Short id);


    @Update({
            "update WCS_Warehouse",
            "set Status = #{status,jdbcType=TINYINT},",
            "Name = #{name,jdbcType=VARCHAR}",
            "where ID = #{id,jdbcType=SMALLINT}"
    })
    int updateByPrimaryKey(WcsWarehouseEntity record);
}