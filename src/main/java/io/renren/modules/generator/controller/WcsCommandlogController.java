package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderDTO;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportAckDTO;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.client.dto.condition.MsgCycleOrderFinishReportAckConditionDTO;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.service.MsgCycleOrderFinishReportAckService;
import io.renren.wap.service.WcsMessageLogService;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.WcsCommandlogEntity;
import io.renren.modules.generator.service.WcsCommandlogService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 命令消息记录表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcscommandlog")
public class WcsCommandlogController {
    @Autowired
    private WcsCommandlogService wcsCommandlogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcscommandlog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsCommandlogService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcscommandlog:info")
    public R info(@PathVariable("id") Integer id){
		WcsCommandlogEntity wcsCommandlog = wcsCommandlogService.getById(id);

        return R.ok().put("wcsCommandlog", wcsCommandlog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcscommandlog:save")
    public R save(@RequestBody WcsCommandlogEntity wcsCommandlog){
		wcsCommandlogService.save(wcsCommandlog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcscommandlog:update")
    public R update(@RequestBody WcsCommandlogEntity wcsCommandlog){
		wcsCommandlogService.updateById(wcsCommandlog);

        return R.ok();
    }
    /**
     * 消息回复
     */
    @RequestMapping("/replay/{id}")
    @RequiresPermissions("generator:wcscommandlog:replay")
    public R replay(@RequestBody @PathVariable("id")Integer id){
        String msg="";
        int code=0;
        WcsCommandlogEntity commandLog = DbUtil.getCommandLogDao().selectById(id);
        String blockName = commandLog.getBlockName();
        String cycleCommand = commandLog.getCycleCommand();
        String mcKey = commandLog.getMckey();
        String plcName = commandLog.getReserved1();
        MsgCycleOrderFinishReportAckConditionDTO msgCycleOrderFinishReportAckConditionDTO = new MsgCycleOrderFinishReportAckConditionDTO(mcKey, plcName, blockName, cycleCommand);
        try {
            MsgCycleOrderFinishReportAckService.sendMsgCycleOrderFinishReportAck(msgCycleOrderFinishReportAckConditionDTO);
            msg=commandLog.getMckey()+"任务回复成功";
            code=0;
        } catch (InterruptedException e) {
            e.printStackTrace();
            msg=commandLog.getMckey()+"任务回复失败";
            code=0;
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工回复23%s消息，结果：%s", id, commandLog.getCommand(), commandLog.toString()));


        return R.error(code,msg);
    }
    /**
     * 手动完成
     */
    @RequestMapping("/finish")
    @RequiresPermissions("generator:wcscommandlog:finish")
    public R finish(@RequestBody WcsCommandlogEntity wcsCommandlog){
        String msg="";
        int code=0;
        WcsCommandlogEntity commandLog = DbUtil.getCommandLogDao().selectById(wcsCommandlog.getId());
        try {
            WcsMachineEntity machine = MachineCache.getMachine(commandLog.getBlockName());
            MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO = new MsgCycleOrderFinishReportDTO();
            msgCycleOrderFinishReportDTO.setCycleCommand(commandLog.getCycleCommand());
            msgCycleOrderFinishReportDTO.setCycleType(commandLog.getCycleType());
            msgCycleOrderFinishReportDTO.setDock(commandLog.getDock());
            msgCycleOrderFinishReportDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
            msgCycleOrderFinishReportDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
            msgCycleOrderFinishReportDTO.setFinishCode(MsgCycleOrderConstant.FINISH_CODE_FINISHED);
            msgCycleOrderFinishReportDTO.setFinishType(MsgCycleOrderConstant.FINISH_TYPE_NORMAL);
            msgCycleOrderFinishReportDTO.setLine(commandLog.getLine());
            msgCycleOrderFinishReportDTO.setTier(commandLog.getTier());
            msgCycleOrderFinishReportDTO.setRow(commandLog.getRow());
            msgCycleOrderFinishReportDTO.setLoadStatus(wcsCommandlog.getLoad());
            msgCycleOrderFinishReportDTO.setMachineName(commandLog.getBlockName());
            msgCycleOrderFinishReportDTO.setMcKey(commandLog.getMckey());
            msgCycleOrderFinishReportDTO.setStation(commandLog.getStation());
            msgCycleOrderFinishReportDTO.setPlcName(machine.getPlcName());
            msgCycleOrderFinishReportDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT);
            msgCycleOrderFinishReportDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
            msgCycleOrderFinishReportDTO.setReSend(MsgConstant.RESEND_SEND);
            msgCycleOrderFinishReportDTO.setSendTime(DateFormatUtil.getStringHHmmss());
            msgCycleOrderFinishReportDTO.setBcc(BccUtil.getBcc(msgCycleOrderFinishReportDTO.getData()));
            MsgQueueCache.addReceiveMsg(machine.getPlcName(), msgCycleOrderFinishReportDTO);
            Log4j2Util.getMsgQueueLogger().info(String.format("received 队列,%s ,人工操作， 接收消息 [%s] —— %s", msgCycleOrderFinishReportDTO.getPlcName(), msgCycleOrderFinishReportDTO.getCommandType(), msgCycleOrderFinishReportDTO.toString()));
            WcsMessageLogService wcsMessageLogService = new WcsMessageLogService();
            wcsMessageLogService.insertIntoWcsMessageLog(machine.getPlcName(), msgCycleOrderFinishReportDTO.getNumString(), MsgConstant.BYTE_TYPE_RECEIVE, "手动完成", "");
            wcsMessageLogService.insertIntoCommandLog(msgCycleOrderFinishReportDTO);
            msg="人工处理消息成功";
            code=0;
        } catch (Exception e) {
            e.printStackTrace();
            msg="人工完成出现异常";
            code=1;
        }

        Log4j2Util.getOperationLog().info(String.format("%s,人工回复设备35消息，结果：%s", wcsCommandlog.getId(), commandLog.getCommand(), commandLog.toString()));


        return R.error(code,msg);
    }
    /**
     * 消息重发
     */
    @RequestMapping("/resend/{id}")
    @RequiresPermissions("generator:wcscommandlog:resend")
    public R resend(@RequestBody @PathVariable("id")Integer id){
        String msg="";
        int code=0;
        WcsCommandlogEntity commandLog = DbUtil.getCommandLogDao().selectById(id);
        String command = commandLog.getCommand();
        String plcName = commandLog.getReserved1();
        if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER.equals(command)) {
            MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
            msgCycleOrderDTO.setMessageNumber(commandLog.getSeqNo());
            msgCycleOrderDTO.setCommandType(commandLog.getCommand());
            msgCycleOrderDTO.setReSend(commandLog.getResend());
            msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
            msgCycleOrderDTO.setPlcName(plcName);
            msgCycleOrderDTO.setDock(commandLog.getDock());
            msgCycleOrderDTO.setStation(commandLog.getStation());
            msgCycleOrderDTO.setTier(commandLog.getTier());
            msgCycleOrderDTO.setLine(commandLog.getLine());
            msgCycleOrderDTO.setRow(commandLog.getRow());
            msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
            msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
            msgCycleOrderDTO.setCycleType(commandLog.getCycleType());
            msgCycleOrderDTO.setCycleCommand(commandLog.getCycleCommand());
            msgCycleOrderDTO.setMachineName(commandLog.getBlockName());
            msgCycleOrderDTO.setMcKey(commandLog.getMckey());
            msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
            try {
                MsgQueueCache.addSendMsg(msgCycleOrderDTO);
                msg="03成功";
                code=0;
            } catch (InterruptedException e) {
                e.printStackTrace();
                msg="消息放入队列失败";
                code=1;
            }
        } else {
            MsgCycleOrderFinishReportAckDTO msgCycleOrderFinishReportAckDTO = new MsgCycleOrderFinishReportAckDTO();
            msgCycleOrderFinishReportAckDTO.setAckType(commandLog.getAckType());
            msgCycleOrderFinishReportAckDTO.setMcKey(commandLog.getMckey());
            msgCycleOrderFinishReportAckDTO.setPlcName(plcName);
            msgCycleOrderFinishReportAckDTO.setBcc(msgCycleOrderFinishReportAckDTO.getData());
            msgCycleOrderFinishReportAckDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            msgCycleOrderFinishReportAckDTO.setSendTime(DateFormatUtil.getStringHHmmss());
            msgCycleOrderFinishReportAckDTO.setReSend(commandLog.getResend());
            msgCycleOrderFinishReportAckDTO.setMessageNumber(commandLog.getSeqNo());
            try {
                MsgQueueCache.addSendMsg(msgCycleOrderFinishReportAckDTO);
                Object object = DbUtil.getBlockDao(commandLog.getBlockName());
                BlockService blockService = new BlockService();
                blockService.updateBlockCommand(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK, object, commandLog.getBlockName());
                msg="05成功";
                code=0;
            } catch (InterruptedException e) {
                e.printStackTrace();
                msg="消息放入队列失败";
                code=1;

            }
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工重发%s消息，结果：%s", id, commandLog.getCommand(), commandLog.toString()));


        return R.error(code,msg);
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcscommandlog:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsCommandlogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
