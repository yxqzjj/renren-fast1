package io.renren.wap.block.task;


import io.renren.wap.entity.constant.BlockConstant;

/**
 * block 判断key类型
 *
 * @Author: CalmLake
 * @date 2019/7/25  16:07
 * @Version: V1.0.0
 **/
public class BlockTaskStatusJudgeByKeyTypeImpl implements BlockTaskStatusJudgeInterface {
    /**
     * block任务标识类型
     */
    private String type;

    public BlockTaskStatusJudgeByKeyTypeImpl(String type) {
        this.type = type;
    }

    /**
     * 判断设备是否可以分配任务
     *
     * @return boolean
     * @author CalmLake
     * @date 2019/7/25 16:04
     */
    @Override
    public boolean isCanTask() {
        return !BlockConstant.KEY_NOT_EMPTY_STRING.equals(type);
    }
}
