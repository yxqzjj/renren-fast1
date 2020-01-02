package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.wap.util.DbUtil;


public class WcsClblockDaoImpl implements BlockDao {
    private WcsClblockDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsClblockDaoImpl INSTANCE = new WcsClblockDaoImpl();
    }
    public static WcsClblockDaoImpl getInstance() {
        return WcsClblockDaoImpl.SingletonInstance.INSTANCE;
    }
  public  int updateTwoCLBlock(String mcKey,String nextBlockName, String name){
    WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
    wcsClblockEntity.setMckey(mcKey);
    wcsClblockEntity.setWithWorkBlockName(nextBlockName);

    return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
}

    public  int updateMcKeyAndLoad(String mcKey,boolean isLoad, String name){
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setMckey(mcKey);
        wcsClblockEntity.setIsLoad(isLoad);

        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
    }

    public  int updateAppointmentMcKeyCLBlock(String mcKey,String nextBlockName, String name){
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setAppointmentMckey(mcKey);
        wcsClblockEntity.setReserved1(nextBlockName);

        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
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
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setIsLoad(isLoad);
        wcsClblockEntity.setCommand(command);
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
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
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setErrorCode(errorCode);
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
    }

    /**
     * 修改mcKey和交互设备编号
     *
     * @param mcKey             mcKey
     * @param name              数据block编号
     * @param withWorkBlockName 交互设备编号
     * @return int
     * @author CalmLake
     * @date 2019/1/9 13:47
     */
    public  int updateMcKeyCLBlock(String mcKey, String withWorkBlockName,String name){
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setMckey(mcKey);
        wcsClblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
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
   public int updateThreeCLBlock( String mcKey, String appointmentMcKey, String withWorkBlockName, String name){
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setMckey(mcKey);
        wcsClblockEntity.setAppointmentMckey(appointmentMcKey);
        wcsClblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
    }

    public  int updateCLBlockLoad(boolean isload, String name){
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setIsLoad(isload);
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
    }
    /**
     * 移载取货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    public   int  updateCLBlockTransplantingPickUpFinished (String name){
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setIsLoad(true);
        wcsClblockEntity.setWithWorkBlockName("");
        wcsClblockEntity.setCommand("35");
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);

    }
    /**
     * 修改停泊设备名称
     *
     * @param name           数据block名称
     * @param berthBlockName 停泊设备名称
     * @return int
     * @author CalmLake
     * @date 2019/1/22 17:36
     */
    public int updateBerthBlockNameByPrimaryKey( String name, String berthBlockName){
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setBerthBlockName(berthBlockName);
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
    }

    /**
     * 移载卸货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
    public   int updateCLBlockTransplantingTheUnloadingFinished(String name){
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setMckey("");
        wcsClblockEntity.setIsLoad(false);
        wcsClblockEntity.setWithWorkBlockName("");
        wcsClblockEntity.setBerthBlockName("");
        wcsClblockEntity.setCommand("35");
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);

    }
    public  WcsClblockEntity selectByPrimaryKey(String name) {
        return DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
    }
    @Override
    public int updateThreeValueBlock(String mcKey, String appointmentMcKey, String withWorkBlockName, String name) {
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setAppointmentMckey(mcKey);
        wcsClblockEntity.setAppointmentMckey(appointmentMcKey);
        wcsClblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);

    }
    @Override
    public int updateAppointmentMcKeyReserved1ByName(String name, String appointmentMcKey, String reserved1) {
        return 0;
    }

    @Override
    public int updateMcKeyByName(String mcKey, String withWorkBlockName, String name) {
        WcsClblockEntity wcsClblockEntity=DbUtil.getCLBlockDao().selectOne(new QueryWrapper<WcsClblockEntity>().eq("Name",name));
        wcsClblockEntity.setAppointmentMckey(mcKey);;
        wcsClblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
    }

    @Override
    public int updateCommandByPrimaryKey(String name, String command) {
        return 0;
    }
}
