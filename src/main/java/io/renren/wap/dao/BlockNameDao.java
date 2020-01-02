package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsBlocknameEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
/**
 * 不可分配任务设备数据集
 * @author CalmLake
 * @date 2019/4/2 16:40
 */
@Repository("BlockNameDao")
@Mapper
public interface BlockNameDao {
    @Insert({
        "insert into WCS_BlockName (Block_Name)",
        "values (#{blockName,jdbcType=CHAR})"
    })
    int insert(WcsBlocknameEntity record);

    /**
     * 统计该block名称数据条数
     * @author CalmLake
     * @date 2019/4/2 16:37
     * @param blockName 数据block名称
     * @return int
     */
    @Select({
            "select count(*) from  WCS_BlockName where Block_Name = #{blockName,jdbcType=CHAR}"
    })
    int countBlockNameNum(@Param("blockName") String blockName);

    /**
     * 清空数据
     * @author CalmLake
     * @date 2019/4/2 16:37
     * @return int
     */
    @Delete({
            "delete WCS_BlockName"
    })
    int deleteAll();
}