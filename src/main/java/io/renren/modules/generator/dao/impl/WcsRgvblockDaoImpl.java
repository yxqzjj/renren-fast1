package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsMcblockEntity;
import io.renren.modules.generator.entity.WcsRgvblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.wap.util.DbUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public class WcsRgvblockDaoImpl implements BlockDao {
    private WcsRgvblockDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsRgvblockDaoImpl INSTANCE = new WcsRgvblockDaoImpl();
    }
    public static WcsRgvblockDaoImpl getInstance() {
        return SingletonInstance.INSTANCE;
    }
  public  int updateTwoRgvBlock(String mcKey,String nextBlockName, String name){
    WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
    wcsRgvblockEntity.setMckey(mcKey);
    wcsRgvblockEntity.setWithWorkBlockName(nextBlockName);

    return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
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
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setIsLoad(isLoad);
        wcsRgvblockEntity.setCommand(command);
        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
    }
    /**
     * 修改设备的载荷状态
     *
     * @param name   数据block名称
     * @param isLoad 载荷状态
     * @return int
     * @author CalmLake
     * @date 2019/3/5 13:57
     */
    public int updateLoad( String name, boolean isLoad){
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setIsLoad(isLoad);
        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
    }
    /**
     * 移动完成修改
     *
     * @param name           数据block名称
     * @param berthBlockName 停泊位置block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:04
     */
    public  int updateMoveFinishByPrimaryKey(String name,String berthBlockName) {
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setCommand("35");
        wcsRgvblockEntity.setBerthBlockName(berthBlockName);

        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
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
   public  int updateThreeRgvBlock(String mcKey, String appointmentMcKey, String withWorkBlockName,String name){
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setMckey(mcKey);
        wcsRgvblockEntity.setAppointmentMckey(appointmentMcKey);
        wcsRgvblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
    }
    /**
     * 移载卸货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
  public   int updateBlockTransplantingTheUnloadingFinished(String name){
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setMckey("");
        wcsRgvblockEntity.setAppointmentMckey("35");
        wcsRgvblockEntity.setIsLoad(false);
        wcsRgvblockEntity.setWithWorkBlockName("");
        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
    }
    /**
     * 移载取货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    public   int updateBlockTransplantingPickUpFinished(String name){
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setAppointmentMckey("35");
        wcsRgvblockEntity.setIsLoad(true);
        wcsRgvblockEntity.setWithWorkBlockName("");
        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
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
    public  int updateBlockErrorCodeByPrimaryKey( String name,String errorCode){
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setErrorCode(errorCode);
        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
    }

    public  int updateTwoValueMLBlock(String mcKey,String nextBlockName, String name){
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setMckey(mcKey);
        wcsRgvblockEntity.setWithWorkBlockName(nextBlockName);

        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
    }

    public  int updateAppointmentMcKeyRGVBlock(String mcKey,String nextBlockName, String name){
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setAppointmentMckey(mcKey);
        wcsRgvblockEntity.setReserved1(nextBlockName);

        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
    }
    public  int updateRGVBlockLoad(boolean isload, String name){
        WcsRgvblockEntity wcsRgvblockEntity=DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
        wcsRgvblockEntity.setIsLoad(isload);

        return DbUtil.getRGVBlockDao().updateById(wcsRgvblockEntity);
    }
    public  WcsRgvblockEntity selectByPrimaryKey(String name){
    return DbUtil.getRGVBlockDao().selectOne(new QueryWrapper<WcsRgvblockEntity>().eq("Name",name));
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
