package io.renren.wap.block.status;

import io.renren.modules.generator.entity.Block;
import io.renren.wap.entity.constant.BlockConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备任务标识判断
 *
 * @Author: CalmLake
 * @date 2019/7/25  11:37
 * @Version: V1.0.0
 **/
public class BlockStatusKeyImpl implements BlockStatusInterface {
    private Block block;

    public BlockStatusKeyImpl(Block block) {
        this.block = block;
    }

    /**
     * 设备任务标识判断状态
     *
     * @return String
     * @author CalmLake
     * @date 2019/7/25 11:32
     */
    @Override
    public String judgeMachineStatus() {
        String type;
        String mcKey = block.getMckey();
        String appointmentMcKey = block.getAppointmentMckey();
        if (StringUtils.isEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
            type = BlockConstant.KEY_EMPTY_STRING;
        } else if (StringUtils.isNotEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
            type = BlockConstant.MCKEY_NOT_EMPTY_STRING;
        } else if (StringUtils.isEmpty(mcKey) && StringUtils.isNotEmpty(appointmentMcKey)) {
            type = BlockConstant.APPOINTMENT_MCKEY_NOT_EMPTY_STRING;
        } else {
            type = BlockConstant.KEY_NOT_EMPTY_STRING;
        }
        return type;
    }
}
