package io.renren.wcs.client.dto.condition;

import java.io.Serializable;

/**
 * 消息05制作条件
 *
 * @Author: CalmLake
 * @date 2019/8/15  10:46
 * @Version: V1.0.0
 **/
public class MsgCycleOrderFinishReportAckConditionDTO implements Serializable {
    private String msgMcKey;
    private String plcName;
    private String blockName;
    private String cycleCommand;

    public MsgCycleOrderFinishReportAckConditionDTO(String msgMcKey, String plcName, String blockName, String cycleCommand) {
        this.msgMcKey = msgMcKey;
        this.plcName = plcName;
        this.blockName = blockName;
        this.cycleCommand = cycleCommand;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getMsgMcKey() {
        return msgMcKey;
    }

    public String getPlcName() {
        return plcName;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCycleCommand() {
        return cycleCommand;
    }
}
