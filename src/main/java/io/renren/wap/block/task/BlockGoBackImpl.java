package io.renren.wap.block.task;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.*;
import io.renren.wap.block.status.AbstractBlockStatus;
import io.renren.wap.block.status.BlockStatus;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.AssigningTaskService;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 回原点任务处理
 *
 * @Author: CalmLake
 * @date 2019/7/30  9:15
 * @Version: V1.0.0
 **/
public class BlockGoBackImpl implements BlockGoBackInterface {
    private WcsMachineEntity machine;

    public BlockGoBackImpl(WcsMachineEntity machine) {
        this.machine = machine;
    }

    /**
     * 回原点任务处理
     *
     * @author CalmLake
     * @date 2019/7/30 9:14
     */
    @Override
    public boolean goBackDefaultLocation() {
        Log4j2Util.getAssigningTaskLogger().info(String.format("block名称：%s，开始返回原点逻辑", machine.getBlockName()));
        AbstractBlockStatus abstractBlockStatus = new BlockStatus(machine);
        abstractBlockStatus.setMachineValues();
        String scBlockName = null;
        String berthBlockName = null;
        Block block = abstractBlockStatus.getBlock();
        String bingScBlockName = null;
        String defaultLocation = machine.getDefaultLocation();
        String scStatus = null;
        if (StringUtils.isNotEmpty(defaultLocation)) {
            int workPlanNum = DbUtil.getTaskingDao().selectCount(new QueryWrapper<WcsTaskingEntity>().eq("Block_Name",machine.getBlockName()).or().eq("Next_Block_Name",machine.getBlockName()));
            int workPlanNum2 = DbUtil.getTaskingDao().selectCount(new QueryWrapper<WcsTaskingEntity>().eq("Next_Block_Name",machine.getBlockName()));
            int workPlanNum3 = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                    .eq("To_Station",machine.getBlockName()).or()
                    .eq("From_Station",machine.getBlockName()).eq("Status",1).or().eq("Status",2).eq("Type",8)
            );
            if (workPlanNum < 1 && workPlanNum2 < 1 && workPlanNum3 < 1) {
                int workPlanNumGoBack = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                        .eq("To_Station",defaultLocation)
                        .eq("From_Station",block.getName())
                        .eq("Status",WorkPlanConstant.STATUS_WAIT).or().eq("Status",WorkPlanConstant.STATUS_WORKING)
                );
                if (workPlanNumGoBack < 1) {
                    if (block instanceof WcsMlblockEntity) {
                        scBlockName = ((WcsMlblockEntity) block).getScBlockName();
                        bingScBlockName = ((WcsMlblockEntity) block).getBingScBlockName();
                        berthBlockName = ((WcsMlblockEntity) block).getBerthBlockName();
                        scStatus = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(bingScBlockName).getStatus();
                    } else if (block instanceof WcsMcblockEntity) {
                        scBlockName = ((WcsMcblockEntity) block).getScBlockName();
                        berthBlockName = ((WcsMcblockEntity) block).getBerthBlockName();
                        bingScBlockName = ((WcsMcblockEntity) block).getBingScBlockName();
                        scStatus = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(bingScBlockName).getStatus();
                    } else {
                        Log4j2Util.getAssigningTaskLogger().info(String.format("block名称：%s，设备类型：%d，未解析的设备类型", block.getName(), machine.getType()));
                    }
                    if (BlockConstant.STATUS_RUNNING.equals(scStatus)) {
                        if (StringUtils.isEmpty(scBlockName) || StringUtils.isEmpty(berthBlockName) || !defaultLocation.equals(berthBlockName)) {
                            WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan("", DateFormatUtil.getStringHHmmss(), "000000000", defaultLocation, "", WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION, machine.getBlockName(), "000000000");
                            AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
                            assigningTaskService.assigningTasks();
                            return true;
                        }
                    } else {
                        Log4j2Util.getAssigningTaskLogger().info(String.format("block名称：%s，穿梭车名称：%s,穿梭车状态：%s，关联穿梭车状态不正确！", block.getName(), bingScBlockName, scStatus));
                    }
                } else {
                    Log4j2Util.getAssigningTaskLogger().info(String.format("block名称：%s，设备类型：%d，该设备已经存在回原点任务了！！！", block.getName(), machine.getType()));
                }
            } else {
                Log4j2Util.getAssigningTaskLogger().info(String.format("block名称：%s，设备类型：%d，该设备正在执行其它任务！！！", block.getName(), machine.getType()));
            }
        }
        return false;
    }
}
