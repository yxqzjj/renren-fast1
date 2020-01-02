package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.*;
import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.wap.util.DbUtil;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


public class WcsAlblockDaoImpl implements BlockDao {
    private WcsAlblockDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsAlblockDaoImpl INSTANCE = new WcsAlblockDaoImpl();
    }
    public static WcsAlblockDaoImpl getInstance() {
        return WcsAlblockDaoImpl.SingletonInstance.INSTANCE;
    }

  public  int updateTwoALBlock(String mcKey,String nextBlockName, String name){
    WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
    wcsAlblockEntity.setMckey(mcKey);
    wcsAlblockEntity.setWithWorkBlockName(nextBlockName);

    return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
}
    public  int updateAppointmentMcKeyALBlock(String mcKey,String nextBlockName, String name){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setAppointmentMckey(mcKey);
        wcsAlblockEntity.setReserved1(nextBlockName);

        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    public  int updateALBlockLoad(boolean isload, String name){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setIsLoad(isload);

        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    /**
     * 修改当前执行命令类型和载荷状态
     *
     * @param name    数据block名称
     * @param command 命令类型
     * @param isLoad  载荷状态
     * @return int
     * @author CalmLake
     * @date 2019/1/16 9:41
     */
   public  int updateCommandAndLoad(String name,String command, boolean isLoad){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setIsLoad(isLoad);
        wcsAlblockEntity.setCommand(command);
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }

    /**
     * 修改当前异常字段
     *
     * @param name      数据block名称
     * @param errorCode 异常码
     * @return int
     * @author CalmLake
     * @date 2019/1/16 16:57
     */
    public int updateBlockErrorCodeByPrimaryKey(String name,String errorCode){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setErrorCode(errorCode);
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    /**
     * 升降机卸车完成
     *
     * @param name              数据block名称
     * @param scBlockName       子车block名称
     * @param mcKey             任务标识
     * @param withWorkBlockName 一起工作设备名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    public  int updateOffCarFinishByPrimaryKey(String name,String mcKey,String withWorkBlockName,String scBlockName){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setMckey(mcKey);
        wcsAlblockEntity.setCommand("35");
        wcsAlblockEntity.setWithWorkBlockName(withWorkBlockName);
        wcsAlblockEntity.setReserved2(scBlockName);
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    /**
     * 升降机接车完成
     *
     * @param name        数据block名称
     * @param scBlockName 子车block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    public int updateGetCarFinishByPrimaryKey(String name,String scBlockName){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setCommand("35");
        wcsAlblockEntity.setReserved2(scBlockName);
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    /**
     * 修改mcKey
     *
     * @param mcKey mcKey
     * @param name  数据block编号
     * @return int
     * @author CalmLake
     * @date 2019/2/22 16:46
     */
    public int updateMcKey(String mcKey,String name){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setMckey(mcKey);
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    /**
     * 移动完成修改
     *
     * @param name           数据block名称
     * @param tier           层
     * @param berthBlockName 停泊位置block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:04
     */
    public  int updateMoveFinishByPrimaryKey(String name,String tier,String berthBlockName){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setCommand("35");
        wcsAlblockEntity.setTier(tier);
        wcsAlblockEntity.setBerthBlockName(berthBlockName);
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    /**
     * 修改mcKey,appointmentMcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param appointmentMcKey  appointmentMcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/9 13:47
     */
    public  int updateThreeALBlock(String mcKey,String appointmentMcKey,String withWorkBlockName, String name){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setMckey(mcKey);
        wcsAlblockEntity.setAppointmentMckey(appointmentMcKey);
        wcsAlblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    /**
     * 移载卸货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
   public int updateBlockTransplantingTheUnloadingFinished(String name){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
        wcsAlblockEntity.setMckey("");
        wcsAlblockEntity.setCommand("35");
        wcsAlblockEntity.setIsLoad(false);
        wcsAlblockEntity.setWithWorkBlockName("");
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    /**
     * 移载取货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    public int updateBlockTransplantingPickUpFinished(String name){
        WcsAlblockEntity wcsAlblockEntity=DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
       wcsAlblockEntity.setCommand("35");
        wcsAlblockEntity.setIsLoad(true);
        wcsAlblockEntity.setWithWorkBlockName("");
        return DbUtil.getALBlockDao().updateById(wcsAlblockEntity);
    }
    public  WcsAlblockEntity selectByPrimaryKey(String name) {
        return DbUtil.getALBlockDao().selectOne(new QueryWrapper<WcsAlblockEntity>().eq("Name",name));
    }

    @Override
    public int updateThreeValueBlock(String mcKey, String appointmentMcKey, String withWorkBlockName, String name) {
        return 0;
    }

    @Override
    public int updateAppointmentMcKeyReserved1ByName(String name, String appointmentMcKey, String reserved1) {
        return 0;
    }

    @Override
    public int updateMcKeyByName(String mcKey, String withWorkBlockName, String name) {
        return 0;
    }

    @Override
    public int updateCommandByPrimaryKey(String name, String command) {
        return 0;
    }
}
