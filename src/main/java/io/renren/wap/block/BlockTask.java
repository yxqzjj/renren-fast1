package io.renren.wap.block;


import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.block.status.BlockStatus;
import io.renren.wap.block.status.BlockStatusKeyImpl;
import io.renren.wap.block.task.BlockGetTaskImpl;
import io.renren.wap.block.task.BlockGoBackImpl;
import io.renren.wap.block.task.BlockTaskImpl;
import io.renren.wap.block.task.BlockTaskStatusJudgeByKeyTypeImpl;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.util.Log4j2Util;
import lombok.Data;

/**
 * 默认任务分配实现
 *
 * @Author: CalmLake
 * @date 2019/7/25  16:55
 * @Version: V1.0.0
 **/
public class BlockTask extends AbstractBlockTask {
    /**
     * 数据block信息
     */
    private Block block;
    /**
     * key 类型
     */
    private String type;
    /**
     * 是否可以分配任务
     */
    private boolean isCanTask;
    /**
     * 待分配任务信息
     */
    private WcsTaskingEntity tasking;
    /**
     * 任务分配结果
     */
    private boolean resultTask;
    /**
     * 回原点任务创建结果标识
     */
    private boolean resultCreateGoBackFlag;

    public BlockTask(WcsMachineEntity machine) {
        this.machine=machine;
    }

    /**
     * 获取设备状态
     *
     * @author CalmLake
     * @date 2019/7/26 15:34
     */
    @Override
    public void getBlockStatus() {
        abstractBlockStatus = new BlockStatus(machine);
        abstractBlockStatus.setMachineValues();
        block = abstractBlockStatus.getBlock();
    }

    @Override
    public void judgeMachineStatus() {
        type = null;
        blockStatusInterface = new BlockStatusKeyImpl(block);
        type = blockStatusInterface.judgeMachineStatus();
    }

    @Override
    public void isCanTask() {
        isCanTask = false;
        blockTaskStatusJudgeInterface = new BlockTaskStatusJudgeByKeyTypeImpl(type);
        isCanTask = blockTaskStatusJudgeInterface.isCanTask();
    }

    @Override
    public void getTask() {
        tasking = null;
        if (isCanTaskBoolean()) {
            blockGetTaskInterface = new BlockGetTaskImpl(machine);
            tasking = blockGetTaskInterface.getTask();
        }
    }

    @Override
    public void task() {
        resultTask = false;
        if (getTasking() != null) {
            blockTaskInterface = new BlockTaskImpl(tasking);
            resultTask = blockTaskInterface.task();
            if (resultTask) {
                Log4j2Util.getAssigningTaskLogger().info(String.format("数据block：%s ，任务分配成功！任务：%s", machine.getBlockName(), tasking.toString()));
            } else {
                Log4j2Util.getAssigningTaskLogger().info(String.format("数据block：%s ，任务分配失败！任务：%s", machine.getBlockName(), tasking.toString()));
            }
        }
    }

    /**
     * 回原点任务处理
     *
     * @author CalmLake
     * @date 2019/7/30 9:14
     */
    @Override
    public void goBackDefaultLocation() {
        resultCreateGoBackFlag = false;
        if (SystemCache.AUTO_BACK_LOCATION) {
            if (BlockConstant.KEY_EMPTY_STRING.equals(getType()) && getTasking() == null) {
                blockGoBackInterface = new BlockGoBackImpl(machine);
                resultCreateGoBackFlag = blockGoBackInterface.goBackDefaultLocation();
            }
        }
    }

    public Block getBlock() {
        return block;
    }

    public String getType() {
        return type;
    }

    public boolean isCanTaskBoolean() {
        return isCanTask;
    }

    public WcsTaskingEntity getTasking() {
        return tasking;
    }

    public boolean isResultTask() {
        return resultTask;
    }

    public boolean isResultCreateGoBackFlag() {
        return resultCreateGoBackFlag;
    }
}
