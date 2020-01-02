package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.entity.WcsMcblockEntity;
import io.renren.wap.util.DbUtil;
import org.apache.ibatis.annotations.Update;

public class WcsMcblockDaoImpl implements BlockDao {
    private WcsMcblockDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsMcblockDaoImpl INSTANCE = new WcsMcblockDaoImpl();
    }
    public static WcsMcblockDaoImpl getInstance() {
        return WcsMcblockDaoImpl.SingletonInstance.INSTANCE;
    }
  public  int updateTwoMcBlock(String mcKey,String nextBlockName, String name){
    WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
    wcsMcblockEntity.setMckey(mcKey);
    wcsMcblockEntity.setWithWorkBlockName(nextBlockName);

    return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
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
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setIsLoad(isLoad);
        wcsMcblockEntity.setCommand(command);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }
    /**
     * 修改设备所载设备名称
     *
     * @param name        数据block名称
     * @param scBlockName 子车名称
     * @return int
     * @author CalmLake
     * @date 2019/3/15 15:04
     */
   public int updateScBlockName(String name,String scBlockName){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setScBlockName(scBlockName);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
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
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setErrorCode(errorCode);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }
    public  int updateAppointmentMcKeyMcBlock(String mcKey,String nextBlockName, String name){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setAppointmentMckey(mcKey);
        wcsMcblockEntity.setReserved1(nextBlockName);

        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }
    public  int updateMcBlockLoad(boolean isload, String name){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setIsLoad(isload);

        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }
    public  WcsMcblockEntity selectByPrimaryKey(String blockName){
    return DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",blockName));
    }
    /**
     * 移载取货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
    public int updateBlockTransplantingPickUpFinished( String name,String loadCar){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setIsLoad(true);
        wcsMcblockEntity.setCommand("35");
        wcsMcblockEntity.setWithWorkBlockName("");
        wcsMcblockEntity.setScBlockName(loadCar);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }
    /**
     * 堆垛机卸车完成
     *
     * @param name              数据block名称
     * @param scBlockName       子车block名称
     * @param isLoad            载货标识
     * @param mcKey             任务标识
     * @param withWorkBlockName 一起工作设备名称
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
   public int updateOffCarFinishByPrimaryKey(String name,String mcKey,String withWorkBlockName,String scBlockName,boolean isLoad){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setIsLoad(isLoad);
        wcsMcblockEntity.setMckey(mcKey);
        wcsMcblockEntity.setCommand("35");
        wcsMcblockEntity.setWithWorkBlockName(withWorkBlockName);
        wcsMcblockEntity.setScBlockName(scBlockName);
        wcsMcblockEntity.setIsMove(false);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }
    /**
     * 堆垛机卸车完成 2 不修改任务标识
     *
     * @param name   数据block名称
     * @param isLoad 载货标识
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    public  int updateOffCarFinish2ByPrimaryKey(String name, boolean isLoad){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setIsLoad(isLoad);
        wcsMcblockEntity.setCommand("35");
        wcsMcblockEntity.setScBlockName("");
        wcsMcblockEntity.setIsMove(false);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }
    /**
     * 堆垛机接车完成
     *
     * @param name        数据block名称
     * @param scBlockName 子车block名称
     * @param isLoad      载货标识
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    public int updateGetCarFinishByPrimaryKey( String name,String scBlockName, boolean isLoad){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setIsLoad(isLoad);
        wcsMcblockEntity.setCommand("35");
        wcsMcblockEntity.setScBlockName(scBlockName);
        wcsMcblockEntity.setIsMove(false);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
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
     * @date 2019/1/24 11:08
     */
    public int updateThreeValueMLBlock(String mcKey, String appointmentMcKey, String withWorkBlockName,String name){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setMckey(mcKey);
        wcsMcblockEntity.setAppointmentMckey(appointmentMcKey);
        wcsMcblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }
    /**
     * 移载卸货完成修改block表
     *
     * @param name 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
    public int updateBlockTransplantingTheUnloadingFinished(String name,String loadCar){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setMckey("");
        wcsMcblockEntity.setCommand("35");
        wcsMcblockEntity.setWithWorkBlockName("");
        wcsMcblockEntity.setIsLoad(false);
        wcsMcblockEntity.setScBlockName(loadCar);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
    }


    /**
     * 移动完成修改
     *
     * @param name           数据block名称
     * @param berthBlockName 停泊位置block名称
     * @param row            排
     * @param line           列
     * @param tier           层
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:04
     */
    public int updateMoveFinishByPrimaryKey(String name,String berthBlockName,String row,String line,String tier, boolean loadStorage,String loadCar){
        WcsMcblockEntity wcsMcblockEntity=DbUtil.getMCBlockDao().selectOne(new QueryWrapper<WcsMcblockEntity>().eq("Name",name));
        wcsMcblockEntity.setBerthBlockName(berthBlockName);
        wcsMcblockEntity.setRow(row);
        wcsMcblockEntity.setLine(line);
        wcsMcblockEntity.setTier(tier);
        wcsMcblockEntity.setScBlockName(loadCar);
        wcsMcblockEntity.setIsLoad(loadStorage);
        wcsMcblockEntity.setIsMove(true);
        return DbUtil.getMCBlockDao().updateById(wcsMcblockEntity);
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
