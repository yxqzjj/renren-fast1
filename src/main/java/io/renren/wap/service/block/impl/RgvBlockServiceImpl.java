package io.renren.wap.service.block.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsRgvblockDaoImpl;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsRgvblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.service.BlockMsgSendService;
import io.renren.wap.service.block.OperationKeyService;
import io.renren.wap.service.msg.RgvMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * RGV逻辑处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  10:17
 * @Version: V1.0.0
 **/
public class RgvBlockServiceImpl extends BlockService implements OperationKeyService {

    @Override
    public void operationKey(String blockName) throws InterruptedException {
        WcsRgvblockEntity rgvBlock = WcsRgvblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        if (isCanSendMsg(rgvBlock)) {
            BlockMsgSendService blockMsgSendService = new BlockMsgSendService();
            MsgDTO msgDTO = doWork(rgvBlock.getMckey(), rgvBlock);
            boolean resultSend = blockMsgSendService.sendMsg(msgDTO, blockName);
            if (resultSend) {
                updateCommand(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, DbUtil.getRGVBlockDao(), blockName);
                blockMsgSendService.resendMsg(msgDTO, blockName);
            }
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s，当前设备不满足消息制作条件！", blockName));
        }
    }

    /**
     * 逻辑处理
     *
     * @param mcKey    mcKey
     * @param rgvBlock RGV状态
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/16 10:19
     */
    private MsgDTO doWork(String mcKey, WcsRgvblockEntity rgvBlock) {
        MsgDTO msgDTO;
        RgvMsgService rgvMsgService = new RgvMsgService();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        Integer workPlanType = workPlan.getType();
        String blockName = rgvBlock.getName();
        String withWorkBlockName = rgvBlock.getWithWorkBlockName();
        String berthBlockName = rgvBlock.getBerthBlockName();
        WcsMachineEntity machine = MachineCache.getMachine(withWorkBlockName);
        if (rgvBlock.getIsLoad()) {
            if (StringUtils.isNotEmpty(berthBlockName) && machine.getDockName().equals(berthBlockName)) {
                // 卸货
                msgDTO = rgvMsgService.transplantingTheUnloading(workPlanType, mcKey, blockName, withWorkBlockName);
            } else {
                // 移动向该设备（输送线）
                msgDTO = rgvMsgService.move(mcKey, blockName, machine.getBlockName(), machine.getDockName());
            }
        } else {
            if (StringUtils.isNotEmpty(berthBlockName) && machine.getDockName().equals(berthBlockName)) {
                // 取货
                msgDTO = rgvMsgService.transplantingPickUp(workPlanType, mcKey, blockName, withWorkBlockName);
            } else {
                // 移动向该设备（输送线）
                msgDTO = rgvMsgService.move(mcKey, blockName, machine.getBlockName(), machine.getDockName());
            }
        }
        return msgDTO;
    }
}
