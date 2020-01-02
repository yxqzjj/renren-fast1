package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.util.DbUtil;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public class WcsTaskingDaoImpl {
    private WcsTaskingDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsTaskingDaoImpl INSTANCE = new WcsTaskingDaoImpl();
    }
    public static WcsTaskingDaoImpl getTaskingDao() {
        return SingletonInstance.INSTANCE;
    }
    @Select({
            "select count(*) from WCS_WcsTaskingEntity where trim(Block_Name) = #{blockName,jdbcType=CHAR} or trim(Next_Block_Name) = #{blockName,jdbcType=CHAR}"
    })
    public int countByBlockName(String blockName){
        return DbUtil.getTaskingDao().selectCount(new QueryWrapper<WcsTaskingEntity>()
                .eq("Block_Name",blockName).or().eq("Next_Block_Name",blockName));
    }
    /**
     * 获取充电未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.Tasking>
     * @author CalmLake
     * @date 2019/7/26 11:22
     */
   public List<WcsTaskingEntity> getChargeWcsTaskingEntityListByBlockName(String blockName){
        return DbUtil.getTaskingDao().selectList(new QueryWrapper<WcsTaskingEntity>()
                .eq("Block_Name",blockName).or()
                .eq("Next_Block_Name",blockName)
                .eq("Work_Plan_Type",6).or()
                .eq("Work_Plan_Type",7).orderByDesc("ID"));
    }
    /**
     * 获取接车卸车换层未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:22
     */
    public List<WcsTaskingEntity> getChangeGetOffCarWcsTaskingEntityListByBlockName(String blockName){
        return DbUtil.getTaskingDao().selectList(new QueryWrapper<WcsTaskingEntity>()
                .eq("Block_Name",blockName).or()
                .eq("Next_Block_Name",blockName)
                .in("Work_Plan_Type",8,9,10,11).orderByDesc("ID"));
    }
    /**
     * 获取移库理货盘点未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:22
     */
    public List<WcsTaskingEntity> getMovementTallyTakeStockWcsTaskingEntityListByBlockName(String blockName){
        return DbUtil.getTaskingDao().selectList(new QueryWrapper<WcsTaskingEntity>()
                .eq("Block_Name",blockName).or()
                .eq("Next_Block_Name",blockName)
                .in("Work_Plan_Type",3,4,5).orderByDesc("ID"));
    }
    /**
     * 获取出库未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:20
     */
    public List<WcsTaskingEntity> getOutPutStorageWcsTaskingEntityListByBlockName(String blockName){
        return DbUtil.getTaskingDao().selectList(new QueryWrapper<WcsTaskingEntity>()
                .eq("Block_Name",blockName).or()
                .eq("Next_Block_Name",blockName)
                .eq("Work_Plan_Type",2).orderByDesc("ID"));
    }
    /**
     * 获取入库未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:19
     */
    public List<WcsTaskingEntity> getPutInStorageWcsTaskingEntityListByBlockName(String blockName){
        return DbUtil.getTaskingDao().selectList(new QueryWrapper<WcsTaskingEntity>()
                .eq("Block_Name",blockName).or()
                .eq("Next_Block_Name",blockName)
                .eq("Work_Plan_Type",1).orderByDesc("ID"));
    }
    /**
     * 根据运行block查找关联任务——出库
     *
     * @param runBlockName 运行block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/8/4 18:32
     */
   public List<WcsTaskingEntity> getClWcsTaskingEntityOutPutListByRunBlockName(String runBlockName){
        return DbUtil.getTaskingDao().selectList(new QueryWrapper<WcsTaskingEntity>()
                .eq("Run_Block_Name",runBlockName)
                .eq("Work_Plan_Type",2).orderByDesc("ID"));
    }

    /**
     * 根据运行block查找关联任务——入库
     *
     * @param runBlockName 运行block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/8/4 18:32
     */
    public List<WcsTaskingEntity> getClWcsTaskingEntityPutInListByRunBlockName(String runBlockName){
        return DbUtil.getTaskingDao().selectList(new QueryWrapper<WcsTaskingEntity>()
                .eq("Run_Block_Name",runBlockName)
                .eq("Work_Plan_Type",1).orderByDesc("ID"));
    }
}
