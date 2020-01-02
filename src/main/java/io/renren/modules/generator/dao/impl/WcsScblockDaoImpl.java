package io.renren.modules.generator.dao.impl;

import cn.hutool.core.lang.Singleton;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsRgvblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.wap.util.DbUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public class WcsScblockDaoImpl implements BlockDao {
    private WcsScblockDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsScblockDaoImpl INSTANCE = new WcsScblockDaoImpl();
    }
    public static WcsScblockDaoImpl getInstance() {
        return SingletonInstance.INSTANCE;
    }
  public  int updateTwoScBlock(String mcKey,String nextBlockName, String name){
    WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
    wcsScblockEntity.setMckey(mcKey);
    wcsScblockEntity.setWithWorkBlockName(nextBlockName);
    return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
}

    public  int updateMcKey(String mcKey, String name){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setMckey(mcKey);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    public  int updateAppointmentMcKey(String appointmentMckey, String name){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setAppointmentMckey(appointmentMckey);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    public  int updateIsUseByName(String name, boolean isUse){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsUse(isUse);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
     public List<WcsScblockEntity> getScBlockListByStandbyCar(boolean isStandbyCar){
        return DbUtil.getSCBlockDao().selectList(new QueryWrapper<WcsScblockEntity>().eq("Is_Standby_Car",isStandbyCar));
    }
    public  int updateAppointmentMcKeyScBlock(String mcKey,String nextBlockName, String name){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setAppointmentMckey(mcKey);
        wcsScblockEntity.setReserved1(nextBlockName);

        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    public  int updateScBlockLoad(boolean isload, String name){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsLoad(isload);

        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    public  int updateStatus(String status, String name){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setStatus(status);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
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
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setCommand(command);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 修改当前异常字段和电量
     *
     * @param name      数据block名称
     * @param errorCode 异常码
     * @param kwh       电量
     * @return int
     * @author CalmLake
     * @date 2019/1/16 16:57
     */
    public int updateBlockErrorCodeAndKWHByPrimaryKey(String kwh,String name,String errorCode){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setErrorCode(errorCode);
        wcsScblockEntity.setKwh(kwh);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }

    /**
     * 充电开始任务完成
     *
     * @param name         该设备数据block
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/4/12 14:18
     */
    public int updateChargeStartByPrimaryKey(String name, Date lastWorkTime){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setStatus("4");
        wcsScblockEntity.setMckey("");
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setBerthBlockName("charge");
        wcsScblockEntity.setIsUse(false);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 修改设备的寄宿状态
     *
     * @param name          数据block名称
     * @param hostBlockName 宿主设备名称
     * @return int
     * @author CalmLake
     * @date 2019/3/5 13:57
     */
    public int updateHostName(String name,String hostBlockName){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setHostBlockName(hostBlockName);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }

    /**
     * 修改设备的载荷状态
     *
     * @param name   数据block名称
     * @param isLoad 载荷状态
     * @param mcKey  任务标识
     * @return int
     * @author CalmLake
     * @date 2019/3/5 13:57
     */
    public int updateLoadMcKey(String name,boolean isLoad, String mcKey){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setMckey(mcKey);
        wcsScblockEntity.setIsLoad(isLoad);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    public  int updateBerthBlockNameByPrimaryKey(String name,  String berthBlockName){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setBerthBlockName(berthBlockName);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
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
    public  int updateBlockErrorCodeByPrimaryKey(String name, String errorCode){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setErrorCode(errorCode);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
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
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsLoad(isLoad);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 充电完成完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    public int updateChargeFinishByPrimaryKey(String name, Date lastWorkTime,String row, String line, String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 盘点任务完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 16:33
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" ='',",
            "\"COMMAND\" =  '35',",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    public int updateTakeStockFinishByPrimaryKey(String name, Date lastWorkTime,  String row,  String line,String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setMckey("");
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 理货任务完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 16:27
     */
    @Update({
            "update WCS_SCBlock",
            "set \"MCKEY\" ='',",
            "\"COMMAND\" =  '35',",
            "\"LAST_WORK_TIME\" =  #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" =  #{row,jdbcType=CHAR},",
            "\"LINE\" =  #{line,jdbcType=CHAR},",
            "\"TIER\" =  #{tier,jdbcType=CHAR}",
            "where   NAME = #{name,jdbcType=CHAR}"
    })
    public int updateTallyFinishByPrimaryKey(String name, Date lastWorkTime, String row,String line,String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setMckey("");
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }

    /**
     * 子车取货完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param isLoad       载货状态
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:09
     */
    public  int updatePickUpFinishByPrimaryKey(String name, boolean isLoad, Date lastWorkTime,
                                                     String row, String line, String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 子车卸货完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param isLoad       载货状态
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:09
     */
    public int updateUnloadFinishByPrimaryKey(String name,boolean isLoad,Date lastWorkTime, String row, String line, String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setMckey("");
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 子车空车下车完成
     *
     * @param name          数据block名称
     * @param hostBlockName 宿主数据block名称
     * @param row           排
     * @param line          列
     * @param tier          层
     * @param isLoad        载货状态
     * @param lastWorkTime  最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    public  int updateOffCarEmptyFinishByPrimaryKey(String name,String hostBlockName, boolean isLoad,Date lastWorkTime, String row, String line,String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setHostBlockName(hostBlockName);
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 子车载货下车完成
     *
     * @param name          数据block名称
     * @param hostBlockName 宿主数据block名称
     * @param row           排
     * @param line          列
     * @param tier          层
     * @param isLoad        载货状态
     * @param lastWorkTime  最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    @Update({
            "update WCS_SCBLOCK",
            "set ",
            "\"COMMAND\" = '35',",
            "\"IS_LOAD\" = #{isLoad,jdbcType=BIT},",
            "\"HOST_BLOCK_NAME\" = #{hostBlockName,jdbcType=CHAR},",
            "\"LAST_WORK_TIME\" = #{lastWorkTime,jdbcType=TIMESTAMP},",
            "\"ROW\" = #{row,jdbcType=CHAR},",
            "\"LINE\" = #{line,jdbcType=CHAR},\"WITH_WORK_BLOCK_NAME\" = '',",
            "\"TIER\" = #{tier,jdbcType=CHAR}",
            "where  Name = #{name,jdbcType=CHAR}"
    })
    public int updateOffCarFinishByPrimaryKey(String name, String hostBlockName, boolean isLoad,Date lastWorkTime,String row,String line, String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setHostBlockName(hostBlockName);
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }

    /**
     * 子车空车上车完成
     *
     * @param name             数据block名称
     * @param appointmentMcKey 预约任务标识
     * @param hostBlockName    宿主数据block名称
     * @param row              排
     * @param line             列
     * @param tier             层
     * @param isLoad           载货状态
     * @param lastWorkTime     最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:27
     */
    public int updateGoOnCarEmptyFinishAppointmentMcKeyByPrimaryKey(String name,String appointmentMcKey,String hostBlockName,boolean isLoad,Date lastWorkTime,String row,String line,String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setAppointmentMckey(appointmentMcKey);
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setHostBlockName(hostBlockName);
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }

    /**
     * 修改穿梭车排列层和载荷信息
     *
     * @param name   block名称
     * @param row    排
     * @param line   列
     * @param tier   层
     * @param isLoad 载荷状态
     * @return int
     * @author CalmLake
     * @date 2019/5/26 13:59
     */
  public   int updateLocationLoadByPrimaryKey(String name,String row, String line, String tier,boolean isLoad){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }

    /**
     * 移动完成
     *
     * @param name         数据block名称
     * @param lastWorkTime 最后一次工作完成时间
     * @param row          排
     * @param line         列
     * @param tier         层
     * @return int
     * @author CalmLake
     * @date 2019/4/10 9:56
     */
    public  int updateMoveFinishByPrimaryKey( String name,Date lastWorkTime,String row,
                                        String line, String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    /**
     * 子车卸货完成
     *
     * @param name         数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @param isLoad       载货状态
     * @param lastWorkTime 最后一次工作时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 15:09
     */
    public  int updateUnloadJiaTianFinishByPrimaryKey(String name, boolean isLoad,Date lastWorkTime,
                                              String row,String line,String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setMckey("");
        wcsScblockEntity.setHostBlockName("");
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
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
    public  int updateThreeScBlock(String mcKey,String appointmentMcKey,String withWorkBlockName, String name){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setAppointmentMckey(appointmentMcKey);
        wcsScblockEntity.setMckey(mcKey);
        wcsScblockEntity.setWithWorkBlockName(withWorkBlockName);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    public   int updateGoOnCarEmptyFinishByPrimaryKey( String name, String mcKey,
                                                            String hostBlockName,boolean isLoad,Date lastWorkTime, String row, String line,String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setMckey(mcKey);
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setHostBlockName(hostBlockName);
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    public   int updateGoOnCarFinishByPrimaryKey( String name,String mcKey, String hostBlockName,
                                       boolean isLoad, Date lastWorkTime, String row,String line,String tier){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setMckey(mcKey);
        wcsScblockEntity.setIsLoad(isLoad);
        wcsScblockEntity.setHostBlockName(hostBlockName);
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }

    public int updateOffCarToClAEmptyFinishByPrimaryKey(String name,String hostBlockName,Date lastWorkTime,String row,String line,String tier,String berthBlockName){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
        wcsScblockEntity.setCommand("35");
        wcsScblockEntity.setBerthBlockName(berthBlockName);
        wcsScblockEntity.setHostBlockName(hostBlockName);
        wcsScblockEntity.setLastWorkTime(lastWorkTime);
        wcsScblockEntity.setRow(row);
        wcsScblockEntity.setTier(tier);
        wcsScblockEntity.setLine(line);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }

    public   int updateReserved2ByName (String name,String reserved2){
        WcsScblockEntity wcsScblockEntity=DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
       wcsScblockEntity.setReserved2(reserved2);
        return DbUtil.getSCBlockDao().updateById(wcsScblockEntity);
    }
    public   WcsScblockEntity selectByPrimaryKey(String name) {
        return DbUtil.getSCBlockDao().selectOne(new QueryWrapper<WcsScblockEntity>().eq("Name",name));
    }
    public int updateThreeValueBlock(String mcKey, String appointmentMcKey, String withWorkBlockName, String name) {
        return 0;
    }

    public int updateAppointmentMcKeyReserved1ByName(String name, String appointmentMcKey, String reserved1) {
        return 0;
    }

    public int updateMcKeyByName(String mcKey, String withWorkBlockName, String name) {
        return 0;
    }

    public  int updateCommandByPrimaryKey(String name, String command) {
        return 0;
    }
}
