package io.renren.wap.thread;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsTaskingDaoImpl;
import io.renren.modules.generator.entity.WcsCrossrouteEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.block.task.BlockTaskClImpl;
import io.renren.wap.block.task.BlockTaskInterface;
import io.renren.wap.entity.constant.CrossRouteConstant;
import io.renren.wap.lock.LockInterface;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.logging.log4j.LogManager;

import java.util.List;

/**
 * 输送线交叉路径指挥
 *
 * @Author: CalmLake
 * @date 2019/8/4  19:29
 * @Version: V1.0.0
 **/
public class MachineClTaskingThread implements Runnable {
    private LockInterface lock;
    private String runBlockName;

    public MachineClTaskingThread(String runBlockName, LockInterface lock) {
        this.lock = lock;
        this.runBlockName = runBlockName;
    }

    @Override
    public void run() {
        LogManager.getLogger().info(String.format("%s,任务分配启动！", runBlockName));
        while (true) {
            try {
                List<WcsTaskingEntity> taskingOutPut = WcsTaskingDaoImpl.getTaskingDao().getClWcsTaskingEntityOutPutListByRunBlockName(runBlockName);
                List<WcsTaskingEntity> taskingPutIn = WcsTaskingDaoImpl.getTaskingDao().getClWcsTaskingEntityPutInListByRunBlockName(runBlockName);
                int workPlanTypeOut = taskingOutPut.size();
                int workPlanTypeIn = taskingPutIn.size();
                WcsTaskingEntity tasking = null;
                WcsCrossrouteEntity crossRoute = DbUtil.getCrossRouteDao().selectOne(new QueryWrapper<WcsCrossrouteEntity>().eq("run_block_name",runBlockName));
                if (workPlanTypeIn > 0 && workPlanTypeOut > 0) {
                    if (CrossRouteConstant.MODE_OUT_PUT == crossRoute.getMode() || CrossRouteConstant.MODE_DEFAULT == crossRoute.getMode()) {
                        if (crossRoute.getLoadNum() < crossRoute.getMaxLoadNum()) {
                            tasking = taskingOutPut.get(0);
                        }
                    }
                } else if (workPlanTypeOut > 0) {
                    if (CrossRouteConstant.MODE_OUT_PUT == crossRoute.getMode() || CrossRouteConstant.MODE_DEFAULT == crossRoute.getMode()) {
                        if (crossRoute.getLoadNum() < crossRoute.getMaxLoadNum()) {
                            tasking = taskingOutPut.get(0);
                        }
                    }
                } else if (workPlanTypeIn > 0) {
                    if (CrossRouteConstant.MODE_PUT_IN == crossRoute.getMode() || CrossRouteConstant.MODE_DEFAULT == crossRoute.getMode()) {
                        if (crossRoute.getLoadNum() < crossRoute.getMaxLoadNum()) {
                            tasking = taskingPutIn.get(0);
                        }
                    }
                }
                if (tasking != null) {
                    BlockTaskInterface blockTaskInterface = new BlockTaskClImpl(tasking);
                    boolean resultTask = blockTaskInterface.task();
                    if (resultTask) {
                        Log4j2Util.getAssigningTaskLogger().info(String.format("运行block：%s ，任务分配成功！任务：%s", runBlockName, tasking.toString()));
                    } else {
                        Log4j2Util.getAssigningTaskLogger().info(String.format("运行block：%s ，任务分配失败！任务：%s", runBlockName, tasking.toString()));
                    }
                }
                lock.await();
            } catch (Exception e) {
                e.printStackTrace();
                Log4j2Util.getAssigningTaskLogger().error(String.format("运行数据block：%s ，任务分配失败！异常：%s", runBlockName, e.getMessage()));
            }
        }
    }
}
