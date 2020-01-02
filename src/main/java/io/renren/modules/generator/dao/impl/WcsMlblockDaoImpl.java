package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.wap.util.DbUtil;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

public  class  WcsMlblockDaoImpl implements BlockDao {
    private WcsMlblockDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsMlblockDaoImpl INSTANCE = new WcsMlblockDaoImpl();
    }
    public static WcsMlblockDaoImpl getInstance() {
        return SingletonInstance.INSTANCE;
    }
    public WcsMlblockEntity selectMLBlockByIsStandbyCarAndStandbyCarBlockName(boolean isStandbyCar,String standbyCarBlockName){
     return DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>()
                .eq("Is_Standby_Car",isStandbyCar).eq("Standby_Car_Block_Name",standbyCarBlockName));
    }
    public int  updateIsStandbyCarByName(String name,boolean isStandbyCar,String standbyCarBlockName){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setIsStandbyCar(isStandbyCar);
        wcsMlblockEntity.setStandbyCarBlockName(standbyCarBlockName);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }
    public WcsMlblockEntity selectByBingScBlockName(String blockName){
    return  DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Bing_SC_Block_Name",blockName));
    }

  public  int updateTwoMlBlock(String mcKey,String nextBlockName, String name){
    WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
    wcsMlblockEntity.setMckey(mcKey);
    wcsMlblockEntity.setWithWorkBlockName(nextBlockName);

    return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
}
    public  int updateAppointmentMcKeyMlBlock(String mcKey,String nextBlockName, String name){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setAppointmentMckey(mcKey);
        wcsMlblockEntity.setReserved1(nextBlockName);

        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }
    public  int updateMlBlockLoad(boolean isload, String name){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setIsLoad(isload);

        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
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
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setIsLoad(isLoad);
        wcsMlblockEntity.setCommand(command);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }

    public   WcsMlblockEntity selectByPrimaryKey(String name) {
        return DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
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
    public int updateOffCarFinish2ByPrimaryKey(String name, boolean isLoad){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
         wcsMlblockEntity.setIsLoad(isLoad);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
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
    public  int updateBlockErrorCodeByPrimaryKey(String name,String errorCode){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setErrorCode(errorCode);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
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
    public  int updateThreeValueMLBlock(String mcKey,String appointmentMcKey,String withWorkBlockName, String name){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setAppointmentMckey(appointmentMcKey);
        wcsMlblockEntity.setMckey(mcKey);
        wcsMlblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
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
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setScBlockName(scBlockName);
        wcsMlblockEntity.setIsLoad(isLoad);
        wcsMlblockEntity.setMckey(mcKey);
        wcsMlblockEntity.setIsMove(false);
        wcsMlblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }
    /**
     * 堆垛机接车完成
     *
     * @param name        数据block名称
     * @param scBlockName 子车block名称
     * @param isLoad      载货标识
     *                    Is_Move =0 (堆垛机二次移动标识)
     * @return int
     * @author CalmLake
     * @date 2019/1/21 14:10
     */
    public  int updateGetCarFinishByPrimaryKey(String name,String scBlockName, boolean isLoad){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setScBlockName(scBlockName);
        wcsMlblockEntity.setIsLoad(isLoad);
        wcsMlblockEntity.setAppointmentMckey("35");
        wcsMlblockEntity.setIsMove(false);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }
    /**
     * 移动完成修改
     *
     * @param name           数据block名称
     * @param berthBlockName 停泊位置block名称
     * @param row            排
     * @param line           列
     * @param tier           层
     *                       Is_Move =1 (堆垛机二次移动标识)
     * @param loadStorage    托盘载荷
     * @param loadCar        子车载荷
     * @return int
     * @author CalmLake
     * @date 2019/1/21 11:04
     */
    public int updateMoveFinishByPrimaryKey( String name,String berthBlockName,
                                      String row,String line,String tier,boolean loadStorage,String loadCar){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setBerthBlockName(berthBlockName);
        wcsMlblockEntity.setIsLoad(loadStorage);
        wcsMlblockEntity.setAppointmentMckey("35");
        wcsMlblockEntity.setIsMove(true);
        wcsMlblockEntity.setRow(row);
        wcsMlblockEntity.setLine(line);
        wcsMlblockEntity.setTier(tier);
        wcsMlblockEntity.setScBlockName(loadCar);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }
    /**
     * 移载卸货完成修改block表
     *
     * @param name    数据block名称
     * @param loadCar 子车载荷
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:45
     */
    public int updateBlockTransplantingTheUnloadingFinished(String name,String loadCar){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
       wcsMlblockEntity.setMckey("");
        wcsMlblockEntity.setIsLoad(false);
        wcsMlblockEntity.setWithWorkBlockName("");
        wcsMlblockEntity.setCommand("35");
        wcsMlblockEntity.setScBlockName(loadCar);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }
    /**
     * 移载取货完成修改block表
     *
     * @param name    数据block名称
     * @param loadCar 子车载荷
     * @return int
     * @author CalmLake
     * @date 2019/1/18 15:50
     */
   public int updateBlockTransplantingPickUpFinished(String name,String loadCar){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setIsLoad(true);
        wcsMlblockEntity.setWithWorkBlockName("");
        wcsMlblockEntity.setCommand("35");
        wcsMlblockEntity.setScBlockName(loadCar);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }
    public int updateScBlockName(String name,String scBlockName){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));
        wcsMlblockEntity.setScBlockName(scBlockName);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
    }
    public  int updateMcKey(String mcKey, String name){
        WcsMlblockEntity wcsMlblockEntity=DbUtil.getMLBlockDao().selectOne(new QueryWrapper<WcsMlblockEntity>().eq("Name",name));

        wcsMlblockEntity.setMckey(mcKey);
        return DbUtil.getMLBlockDao().updateById(wcsMlblockEntity);
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
