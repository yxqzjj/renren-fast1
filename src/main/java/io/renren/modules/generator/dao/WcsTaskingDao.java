package io.renren.modules.generator.dao;

import io.renren.modules.generator.dao.provider.TaskingProvider;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 任务分配表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@Mapper
public interface WcsTaskingDao extends BaseMapper<WcsTaskingEntity> {
    /**
     * 插入
     *
     * @param record 任务信息
     * @return int
     * @author CalmLake
     * @date 2019/1/9 14:45
     */
    @InsertProvider(type = TaskingProvider.class, method = "insertProvider")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int insertProvider(WcsTaskingEntity record);
    /**
     * 查找待分配任务 使用存储过程  该sql适用于单仓库双堆垛机 充电时一台子车会使用两台堆垛机
     *
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/4/22 13:29
     */
    @Select({
            "exec sp_select_tasking_out"
    })
    List<WcsTaskingEntity> getOneWarehouseListByProc();

    /**
     * 查找待分配任务 使用存储过程  该sql适用于多仓库双堆垛机且独立运行
     *
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/4/22 13:29
     */
    @Select({
            "exec sp_select_tasking_warehouses_out"
    })
    List<WcsTaskingEntity> getWarehousesListByProc();
}
