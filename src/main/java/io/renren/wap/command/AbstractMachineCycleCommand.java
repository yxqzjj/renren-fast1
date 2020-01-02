package io.renren.wap.command;


import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.client.dto.condition.MsgCycleOrderFinishReportAckConditionDTO;

/**
 * 设备动作实际处理类
 *
 * @Author: CalmLake
 * @Date: 2019/6/6  15:54
 * @Version: V1.0.0
 **/
public abstract class AbstractMachineCycleCommand {
    /**
     * 35完成报告
     */
    public MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO;
    /**
     * 当前执行命令的数据block名称
     */
    public String blockName;
    /**
     * 当前执行命令的plc名称
     */
    public String plcName;
    /**
     * 当前执行命令的货物唯一标识
     */
    public String msgMcKey;
    /**
     * 当前执行命令执行的动作指令
     */
    public String cycleCommand;
    /**
     * 当前执行命令中的完成类型
     */
    public String finishType;
    /**
     * 当前执行命令中的完成代码
     */
    public String finishCode;
    /**
     * 当前执行命令中的载荷类型
     */
    public String loadStatus;
    /**
     * 当前执行命令中的排
     */
    public String row;
    /**
     * 当前执行命令中的列
     */
    public String line;
    /**
     * 当前执行命令中的层
     */
    public String tier;

    public MsgCycleOrderFinishReportAckConditionDTO msgCycleOrderFinishReportAckConditionDTO;

    public AbstractMachineCycleCommand(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) {
        this.msgCycleOrderFinishReportDTO = msgCycleOrderFinishReportDTO;
        this.blockName = msgCycleOrderFinishReportDTO.getMachineName();
        this.plcName = msgCycleOrderFinishReportDTO.getPlcName();
        this.msgMcKey = msgCycleOrderFinishReportDTO.getMcKey();
        this.cycleCommand = msgCycleOrderFinishReportDTO.getCycleCommand();
        this.finishType = msgCycleOrderFinishReportDTO.getFinishType();
        this.finishCode = msgCycleOrderFinishReportDTO.getFinishCode();
        this.loadStatus = msgCycleOrderFinishReportDTO.getLoadStatus();
        this.row = msgCycleOrderFinishReportDTO.getRow();
        this.line = msgCycleOrderFinishReportDTO.getLine();
        this.tier = msgCycleOrderFinishReportDTO.getTier();
        msgCycleOrderFinishReportAckConditionDTO = new MsgCycleOrderFinishReportAckConditionDTO(msgMcKey,plcName,blockName,cycleCommand);
    }

    /**
     * 处理消息
     * @author CalmLake
     * @date 2019/6/6 17:00
     * @throws InterruptedException 线程中断异常
     */
    public abstract void execute() throws InterruptedException;
}
