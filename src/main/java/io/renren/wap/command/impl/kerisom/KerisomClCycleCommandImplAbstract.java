package io.renren.wap.command.impl.kerisom;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsClblockDaoImpl;
import io.renren.modules.generator.entity.WcsCrossrouteEntity;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.impl.ClCycleCommandImplAbstract;
import io.renren.wap.entity.constant.CrossRouteConstant;
import io.renren.wap.entity.constant.TaskingConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.MsgCycleOrderFinishReportAckService;
import io.renren.wap.service.RouteService;
import io.renren.wap.service.TaskingService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 伽力森输送线动作处理
 *
 * @Author: CalmLake
 * @Date: 2019/6/6  16:31
 * @Version: V1.0.0
 **/
public class KerisomClCycleCommandImplAbstract extends ClCycleCommandImplAbstract {
    public KerisomClCycleCommandImplAbstract(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) {
        super(msgCycleOrderFinishReportDTO);
    }

    @Override
    public void pickup() throws InterruptedException {
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
        int workPlanType = workPlan.getType();
        String toStation = workPlan.getToStation();
        int priority = workPlan.getPriorityConfigPriority();
        int resultUpdateDb = WcsClblockDaoImpl.getInstance().updateCLBlockTransplantingPickUpFinished(blockName);
        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，mcKey：%s，移栽取货处理结果：%d", blockName, msgMcKey, resultUpdateDb));
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        String station = machine.getStationName();
        if (StringUtils.isNotEmpty(station) && StringUtils.isNotEmpty(toStation) && toStation.equals(station) && WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，mcKey：%s，货物已到达站台：%s", blockName, msgMcKey, station));
        } else {
            String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
            WcsCrossrouteEntity crossRoute = DbUtil.getCrossRouteDao().selectOne(new QueryWrapper<WcsCrossrouteEntity>().eq("runBlockName",machine.getReserved1()));
            if (crossRoute == null) {
                WcsMachineEntity machineNext = MachineCache.getMachine(nextBlockName);
                WcsCrossrouteEntity crossRouteNext = DbUtil.getCrossRouteDao().selectOne(new QueryWrapper<WcsCrossrouteEntity>().eq("runBlockName",machine.getReserved1()));
                if (crossRouteNext == null) {
                    transplantingPickUpFinishedOperation(nextBlockName, msgMcKey, blockName, priority, workPlanType,toStation);
                } else {
                    //  下一设备属于交叉路径 当前设备不属于
                    WcsClblockDaoImpl.getInstance().updateTwoCLBlock(msgMcKey, nextBlockName, blockName);
                    //  放入任务分配表
                    TaskingService taskingService = new TaskingService();
                    taskingService.insertNewTasking(msgMcKey, nextBlockName, blockName, priority, workPlanType, TaskingConstant.ML_MC_ZERO, machine.getWarehouseNo(),machineNext.getReserved1(),toStation,TaskingConstant.MACHINE_TYPE_CL_CL);
                    LockCache.getValue(crossRouteNext.getRunBlockName()).signal();
                }
            } else {
                transplantingPickUpFinishedOperation(nextBlockName, msgMcKey, blockName, priority, workPlanType,toStation);
            }
        }
    }

    @Override
    public void unload() throws InterruptedException {
        int resultUpdateDb = WcsClblockDaoImpl.getInstance().updateCLBlockTransplantingTheUnloadingFinished(blockName);
        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，mcKey：%s，移栽卸货处理结果：%d", blockName, msgMcKey, resultUpdateDb));
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        String withWorkBlockName = clBlock.getWithWorkBlockName();
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        WcsCrossrouteEntity crossRoute = DbUtil.getCrossRouteDao().selectOne(new QueryWrapper<WcsCrossrouteEntity>().eq("runBlockName",machine.getReserved1()));
        if (crossRoute == null) {
            transplantingTheUnloadingFinished(blockName);
        } else {
            WcsMachineEntity machineWithWork = MachineCache.getMachine(withWorkBlockName);
            WcsCrossrouteEntity crossRouteWithWork = DbUtil.getCrossRouteDao().selectOne(new QueryWrapper<WcsCrossrouteEntity>().eq("runBlockName",machine.getReserved1()));
            if (crossRouteWithWork == null) {
                //  交叉路径向非交叉路径卸货 交叉路径货物减1
                WcsCrossrouteEntity crossRoute1=new WcsCrossrouteEntity();
                Integer loadNum = (crossRoute.getLoadNum() - 1);
                if (loadNum < 1) {
                    crossRoute1.setLoadNum(0);
                    crossRoute1.setMode(CrossRouteConstant.MODE_DEFAULT);
                } else {
                    crossRoute1.setLoadNum(loadNum);
                    crossRoute1.setMode(crossRoute.getMode());
                }
                crossRoute1.setRunBlockName(crossRoute.getRunBlockName());
                crossRoute1.setMaxLoadNum(crossRoute.getMaxLoadNum());
                DbUtil.getCrossRouteDao().update(crossRoute1,(Wrapper<WcsCrossrouteEntity>) new Object());
                transplantingTheUnloadingFinished(blockName,machine.getReserved1());
            } else {
                transplantingTheUnloadingFinished(blockName);
            }
            LockCache.getValue(crossRoute.getRunBlockName()).signal();
        }
    }
}
