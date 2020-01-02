package io.renren.wap.control;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.renren.modules.generator.entity.WcsCommandlogEntity;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;
import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.*;
import io.renren.wap.client.dto.condition.MsgCycleOrderFinishReportAckConditionDTO;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.client.util.MessageDetailUtil;
import io.renren.wap.constant.DatePatternConstant;
import io.renren.wap.dao.CommandLogDao;
import io.renren.wap.dao.MachineDao;
import io.renren.wap.dao.WCSMessageLogDao;
import io.renren.wap.dao.WMSMessageLogDao;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.xml.util.XStreamUtil;
import io.renren.wap.service.MsgCycleOrderFinishReportAckService;
import io.renren.wap.service.WcsMessageLogService;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息处理
 *
 * @Author: CalmLake
 * @Date: 2019/2/21  16:04
 * @Version: V1.0.0
 **/
@Controller
@RequestMapping("messageControl")
public class MessageControl {
    @Resource(name = "CommandLogDao")
    private CommandLogDao commandLogDao;
    @Resource(name = "MachineDao")
    private MachineDao machineDao;
    @Resource(name = "WCSMessageLogDao")
    private WCSMessageLogDao wcsMessageLogDao;
    @Resource(name = "WMSMessageLogDao")
    private WMSMessageLogDao wmsMessageLogDao;

    @RequestMapping("resendWmsMessage")
    @ResponseBody
    public JSONObject resendWmsMessage(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id");
        try {
            WcsWmsmessagelogEntity wmsMessageLog = wmsMessageLogDao.selectByPrimaryKey(Integer.parseInt(id));
            XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, XStreamUtil.stringToEnvelopeDto(wmsMessageLog.getMessage()));
            jsonObject.put("result", true);
            jsonObject.put("msg", "人工处理消息成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "人工完成出现异常:" + e.getMessage());
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工发送wms消息，结果：%s", id, jsonObject.toJSONString()));
        return jsonObject;
    }

    @RequestMapping("getListWmsMessage")
    @ResponseBody
    public JSONArray getListWmsMessage() {
        JSONArray jsonArray = new JSONArray();
        List<WcsWmsmessagelogEntity> wmsMessageLogList = wmsMessageLogDao.getList();
        for (WcsWmsmessagelogEntity wmsMessageLog : wmsMessageLogList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", wmsMessageLog.getId());
            jsonObject.put("status", wmsMessageLog.getStatus());
            jsonObject.put("barcode", wmsMessageLog.getBarcode());
            jsonObject.put("createTime", DateFormatUtil.dateToString(wmsMessageLog.getCreateTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
            jsonObject.put("message", wmsMessageLog.getMessage().trim());
            jsonObject.put("type", wmsMessageLog.getType());
            jsonObject.put("uuid", wmsMessageLog.getUuid());
            jsonObject.put("wmdID", wmsMessageLog.getWmsId());
            jsonObject.put("workPlanId", wmsMessageLog.getWorkPlanId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 手动动作完成操作
     *
     * @param request 请求数据 id-消息记录序号，loadStatus-载荷状态
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/6/6 9:58
     */
    @RequestMapping("operationFinished")
    @ResponseBody
    public JSONObject operationFinished(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String idString = request.getParameter("id");
        try {
            String id = idString.substring(1, idString.length() - 1);
            String loadStatus = request.getParameter("loadStatus");
            WcsCommandlogEntity commandLog = DbUtil.getCommandLogDao().selectById(Integer.parseInt(id));
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
            msgCycleOrderFinishReportDTO.setLoadStatus(loadStatus);
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
            jsonObject.put("result", true);
            jsonObject.put("msg", "人工处理消息成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "人工完成出现异常");
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工回复设备35消息，结果：%s", idString, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 获取wcs-plc消息记录
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/27 10:11
     */
    @RequestMapping("getMsgLog")
    @ResponseBody
    public JSONArray getMsgLog() {
        JSONArray jsonArray = new JSONArray();
        List<WcsWcsmessagelogEntity> wcsMessageLogList = wcsMessageLogDao.getTop100DescList();
        for (WcsWcsmessagelogEntity wcsMessageLog : wcsMessageLogList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", wcsMessageLog.getId());
            jsonObject.put("createTime", DateFormatUtil.dateToString(wcsMessageLog.getCreateTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
            jsonObject.put("msg", wcsMessageLog.getMessage());
            jsonObject.put("plcName", wcsMessageLog.getPlcName());
            jsonObject.put("type", getWcsTypeString(wcsMessageLog.getType()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取站台信息
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/27 10:11
     */
    @RequestMapping("getStationList")
    @ResponseBody
    public JSONArray getStationList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsMachineEntity> machineList = machineDao.selectStationList();
        for (WcsMachineEntity machine : machineList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("station", machine.getStationName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取plc信息
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/27 10:11
     */
    @RequestMapping("getPlcList")
    @ResponseBody
    public JSONArray getPlcList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsMachineEntity> machineList = machineDao.selectPlcList();
        for (WcsMachineEntity machine : machineList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("plcName", machine.getPlcName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取block信息
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/27 10:12
     */
    @RequestMapping("getBlockList")
    @ResponseBody
    public JSONArray getBlockList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsMachineEntity> machineList = machineDao.selectBlockList();
        for (WcsMachineEntity machine : machineList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("block", machine.getBlockName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取运行block信息
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/27 10:12
     */
    @RequestMapping("getRunBlockList")
    @ResponseBody
    public JSONArray getRunBlockList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsMachineEntity> machineList = machineDao.selectRunBlockList();
        for (WcsMachineEntity machine : machineList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("runBlock", machine.getReserved1());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @RequestMapping("sendMsg")
    @ResponseBody
    public JSONObject sendMsg(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String msg = request.getParameter("msg");
        if (StringUtils.isEmpty(msg)) {
            jsonObject.put("result", false);
            jsonObject.put("msg", "数据为空");
            return jsonObject;
        }
        JSONObject jsonObjectData = JSONObject.parseObject(msg);
        String commandType = jsonObjectData.containsKey("commandType") ? jsonObjectData.getString("commandType") : "";
        if (StringUtils.isEmpty(commandType)) {
            jsonObject.put("result", false);
            jsonObject.put("msg", "commandType数据为空");
            return jsonObject;
        } else {
            MsgDTO msgDTO = createMsg(jsonObjectData);
            try {
                if (msgDTO == null) {
                    jsonObject.put("result", false);
                    jsonObject.put("msg", "消息制作为空");
                    return jsonObject;
                }
                MsgQueueCache.addSendMsg(msgDTO);
                jsonObject.put("result", true);
                jsonObject.put("msg", "消息制作成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
                jsonObject.put("result", false);
                jsonObject.put("msg", "消息制作放入队列失败");
            }
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工发送消息，结果：%s", msg, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 手动回复05消息
     *
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/2/22 10:31
     */
    @RequestMapping("replay")
    @ResponseBody
    public JSONObject replay(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String idString = request.getParameter("msg");
        int idInt = Integer.parseInt(idString);
        WcsCommandlogEntity commandLog = commandLogDao.getCommandLogById(idInt);
        String blockName = commandLog.getBlockName();
        String cycleCommand = commandLog.getCycleCommand();
        String mcKey = commandLog.getMckey();
        String plcName = commandLog.getReserved1();
        MsgCycleOrderFinishReportAckConditionDTO msgCycleOrderFinishReportAckConditionDTO = new MsgCycleOrderFinishReportAckConditionDTO(mcKey, plcName, blockName, cycleCommand);
        try {
            MsgCycleOrderFinishReportAckService.sendMsgCycleOrderFinishReportAck(msgCycleOrderFinishReportAckConditionDTO);
            jsonObject.put("result", true);
            jsonObject.put("msg", "成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "消息放入队列失败");
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工回复05消息，结果：%s", idString, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 消息重发
     *
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/2/22 10:15
     */
    @RequestMapping("resend")
    @ResponseBody
    public JSONObject resend(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String idString = request.getParameter("msg");
        int idInt = Integer.parseInt(idString);
        WcsCommandlogEntity commandLog = commandLogDao.getCommandLogById(idInt);
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
                jsonObject.put("result", true);
                jsonObject.put("msg", "成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
                jsonObject.put("result", false);
                jsonObject.put("msg", "消息放入队列失败");
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
                jsonObject.put("result", true);
                jsonObject.put("msg", "成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
                jsonObject.put("result", false);
                jsonObject.put("msg", "消息放入队列失败");
            }
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工重发%s消息，结果：%s", idString, commandLog.getCommand(), jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 获取所有命令消息
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/22 9:51
     */
    @RequestMapping("getMessageArray")
    @ResponseBody
    public JSONArray getMessageArray(HttpServletRequest request) {
        String mcKey = request.getParameter("mcKey");
        String blockName = request.getParameter("blockName");
        JSONArray jsonArray = new JSONArray();
        List<WcsCommandlogEntity> commandLogList;
        if (StringUtils.isEmpty(mcKey) && StringUtils.isEmpty(blockName)) {
            commandLogList = commandLogDao.getList();
        } else if (StringUtils.isEmpty(mcKey) && StringUtils.isNotEmpty(blockName)) {
            commandLogList = commandLogDao.selectByBlockName(blockName);
        } else if (StringUtils.isNotEmpty(mcKey) && StringUtils.isEmpty(blockName)) {
            commandLogList = commandLogDao.selectByMcKey(mcKey);
        } else {
            commandLogList = commandLogDao.selectByMcKeyBlockName(mcKey, blockName);
        }
        for (WcsCommandlogEntity commandLog : commandLogList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", commandLog.getId());
            jsonObject.put("Command", commandLog.getCommand());
            jsonObject.put("Seq_No", commandLog.getSeqNo());
            jsonObject.put("Create_Time", DateFormatUtils.format(commandLog.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            jsonObject.put("Block_Name", commandLog.getBlockName());
            jsonObject.put("Cycle_Command", MessageDetailUtil.getCycleCommandDetail(commandLog.getCycleCommand() == null ? "" : commandLog.getCycleCommand()));
            jsonObject.put("Cycle_Type", MessageDetailUtil.getCycleTypeDetail(commandLog.getCycleType() == null ? "" : commandLog.getCycleType()));
            jsonObject.put("McKey", commandLog.getMckey() == null ? "" : commandLog.getMckey());
            jsonObject.put("Station", commandLog.getStation() == null ? "" : commandLog.getStation());
            jsonObject.put("Dock", commandLog.getDock() == null ? "" : commandLog.getDock());
            jsonObject.put("Tier", commandLog.getTier() == null ? "" : commandLog.getTier());
            jsonObject.put("Line", commandLog.getLine() == null ? "" : commandLog.getLine());
            jsonObject.put("Row", commandLog.getRow() == null ? "" : commandLog.getRow());
            jsonObject.put("Load", MessageDetailUtil.getLoadDetail(commandLog.getLoad() == null ? "" : commandLog.getLoad()));
            jsonObject.put("Ack_Type", commandLog.getAckType() == null ? "" : commandLog.getAckType());
            jsonObject.put("Error_Type", commandLog.getErrorType() == null ? "" : commandLog.getErrorType());
            jsonObject.put("Finish_Type", commandLog.getFinishType() == null ? "" : commandLog.getFinishType());
            jsonObject.put("Finish_Code", commandLog.getReserved2() == null ? "" : commandLog.getReserved2());
            jsonObject.put("Resend", commandLog.getResend() == null ? "" : commandLog.getResend());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取wcs消息类型字符串
     *
     * @param type 类型
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/2/27 10:10
     */
    private String getWcsTypeString(int type) {
        switch (type) {
            case 1:
                return "发送";
            case 2:
                return "接收";
            default:
                return "";
        }
    }


    /**
     * 制作消息
     *
     * @param jsonObject 表单信息
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/2/26 15:50
     */
    private MsgDTO createMsg(JSONObject jsonObject) {
        String msgNo;
        String sendTime;
        String data;
        String plcName;
        String blockName;
        String reSend;
        String mcKey;
        String cargoWidth;
        String rackRow;
        String rackLine;
        String rackTier;
        String station;
        String wharf;
        String cargoHigh;
        String cycleCommand;
        String jobType;
        String commandType;
        commandType = jsonObject.getString("commandType");
        msgNo = jsonObject.getString("msgNO");
        sendTime = jsonObject.getString("sendTime");
        reSend = jsonObject.getString("reSend");
        if (MsgConstant.MSG_COMMAND_TYPE_START_MACHINERY.equals(commandType)) {
            MsgStartMachineryDTO msgDTO = new MsgStartMachineryDTO();
            data = jsonObject.getString("data");
            blockName = jsonObject.getString("blockName");
            plcName = machineDao.selectPlcName(blockName).getPlcName();
            msgDTO.setDataNum(data);
            List<String> stringList = new ArrayList<>();
            stringList.add(blockName);
            msgDTO.setMachineNameList(stringList);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(plcName);
            return msgDTO;
        } else if (MsgConstant.MSG_COMMAND_TYPE_STOP_MACHINERY.equals(commandType)) {
            MsgStopMachineryDTO msgDTO = new MsgStopMachineryDTO();
            data = jsonObject.getString("data");
            blockName = jsonObject.getString("blockName");
            plcName = machineDao.selectPlcName(blockName).getPlcName();
            msgDTO.setDataNum(data);
            List<String> stringList = new ArrayList<>();
            stringList.add(blockName);
            msgDTO.setMachineNameList(stringList);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(plcName);
            return msgDTO;
        } else if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER.equals(commandType)) {
            msgNo = jsonObject.getString("msgNO");
            sendTime = jsonObject.getString("sendTime");
            reSend = jsonObject.getString("reSend");
            mcKey = jsonObject.getString("mcKey");
            blockName = jsonObject.getString("blockName");
            cargoWidth = jsonObject.getString("cargoWidth");
            rackRow = jsonObject.getString("rackRow");
            rackLine = jsonObject.getString("rackLine");
            rackTier = jsonObject.getString("rackTier");
            station = jsonObject.getString("station");
            wharf = jsonObject.getString("dock");
            cargoHigh = jsonObject.getString("cargoHigh");
            cycleCommand = jsonObject.getString("cycleCommand");
            jobType = jsonObject.getString("jobType");
            MsgCycleOrderDTO msgDTO = new MsgCycleOrderDTO();
            plcName = machineDao.selectByBlockName(blockName).getPlcName();
            msgDTO.setMcKey(mcKey);
            msgDTO.setMachineName(blockName);
            msgDTO.setCycleCommand(cycleCommand);
            msgDTO.setDock(wharf);
            msgDTO.setStation(station);
            msgDTO.setCycleType(jobType);
            msgDTO.setHeight(cargoHigh);
            msgDTO.setWidth(cargoWidth);
            msgDTO.setLine(rackLine);
            msgDTO.setTier(rackTier);
            msgDTO.setRow(rackRow);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(plcName);
            return msgDTO;
        } else if (MsgConstant.MSG_COMMAND_TYPE_DELETE_DATA.equals(commandType)) {
            String orderType = jsonObject.getString("orderType");
            msgNo = jsonObject.getString("msgNO");
            sendTime = jsonObject.getString("sendTime");
            reSend = jsonObject.getString("reSend");
            mcKey = jsonObject.getString("mcKey");
            blockName = jsonObject.getString("blockName");
            MsgDeleteDataDTO msgDTO = new MsgDeleteDataDTO();
            plcName = machineDao.selectByBlockName(blockName).getPlcName();
            msgDTO.setOperationType(orderType);
            msgDTO.setMcKey(mcKey);
            msgDTO.setMachineName(blockName);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(plcName);
            return msgDTO;
        } else if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK.equals(commandType)) {
            String ackType = jsonObject.getString("ackType");
            msgNo = jsonObject.getString("msgNO");
            sendTime = jsonObject.getString("sendTime");
            reSend = jsonObject.getString("reSend");
            mcKey = jsonObject.getString("mcKey");
            plcName = jsonObject.getString("plcName");
            MsgCycleOrderFinishReportAckDTO msgDTO = new MsgCycleOrderFinishReportAckDTO();
            msgDTO.setCycleCommand("00");
            msgDTO.setBlockName("0000");
            msgDTO.setMcKey(mcKey);
            msgDTO.setAckType(ackType);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(plcName);
            return msgDTO;
        } else if (MsgConstant.MSG_COMMAND_TYPE_MACHINERY_STATUS_ORDER_ASK.equals(commandType)) {
            String checkStatus = jsonObject.getString("checkStatus");
            blockName = jsonObject.getString("machineName");
            msgNo = jsonObject.getString("msgNO");
            sendTime = jsonObject.getString("sendTime");
            reSend = jsonObject.getString("reSend");
            MsgMachineryStatusOrderAskDTO msgDTO = new MsgMachineryStatusOrderAskDTO();
            plcName = machineDao.selectPlcName(blockName).getPlcName();
            msgDTO.setMachineName(blockName);
            msgDTO.setStatus(checkStatus);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(plcName);
            return msgDTO;
        } else if (MsgConstant.MSG_COMMAND_TYPE_HEART_BEAT_SIGNAL_ASK.equals(commandType)) {
            String heartBeat = jsonObject.getString("heartBeat");
            String wcsNo = jsonObject.getString("wcsNo");
            String consoleNo = jsonObject.getString("consoleNo");
            msgNo = jsonObject.getString("msgNO");
            sendTime = jsonObject.getString("sendTime");
            reSend = jsonObject.getString("reSend");
            MsgHeartBeatSignalAskDTO msgDTO = new MsgHeartBeatSignalAskDTO();
            msgDTO.setConsoleNo(consoleNo);
            msgDTO.setWcsNo(wcsNo);
            msgDTO.setHeartBeat(heartBeat);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(consoleNo);
            return msgDTO;
        } else if (MsgConstant.MSG_COMMAND_TYPE_CHANGE_STATION_MODE.equals(commandType)) {
            station = jsonObject.getString("station");
            String mode = jsonObject.getString("mode");
            msgNo = jsonObject.getString("msgNO");
            sendTime = jsonObject.getString("sendTime");
            reSend = jsonObject.getString("reSend");
            MsgChangeStationModeDTO msgDTO = new MsgChangeStationModeDTO();
            plcName = machineDao.selectByStationName(station).getPlcName();
            msgDTO.setMode(mode);
            msgDTO.setStation(station);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(plcName);
            return msgDTO;
        } else if (MsgConstant.MSG_COMMAND_TYPE_CONVEYORLINE_DATA_REPORT_ACK.equals(commandType)) {
            msgNo = jsonObject.getString("msgNO");
            sendTime = jsonObject.getString("sendTime");
            reSend = jsonObject.getString("reSend");
            mcKey = jsonObject.getString("mcKey");
            cargoWidth = jsonObject.getString("cargoWidth");
            cargoHigh = jsonObject.getString("cargoHigh");
            station = jsonObject.getString("station");
            String dataNum = jsonObject.getString("dataNum");
            String loadCount = jsonObject.getString("loadCount");
            String barcode = jsonObject.getString("barcode");
            String loadStatus = jsonObject.getString("loadStatus");
            String cargoWeight = jsonObject.getString("cargoWeight");
            MsgConveyorLineDataReportAckDTO msgDTO = new MsgConveyorLineDataReportAckDTO();
            plcName = machineDao.selectByStationName(station).getPlcName();
            msgDTO.setMcKey(mcKey);
            msgDTO.setWeight(cargoWeight);
            msgDTO.setCargoHeight(cargoHigh);
            msgDTO.setCargoWidth(cargoWidth);
            msgDTO.setDataNum(dataNum);
            msgDTO.setLoadStatus(loadStatus);
            msgDTO.setBarcode(barcode);
            msgDTO.setStorageNum(loadCount);
            msgDTO.setBlockNo(station);
            msgDTO.setBcc(BccUtil.getBcc(msgDTO.getData()));
            msgDTO.setCommandType(commandType);
            msgDTO.setMessageNumber(msgNo);
            msgDTO.setSendTime(sendTime);
            msgDTO.setReSend(reSend);
            msgDTO.setPlcName(plcName);
            return msgDTO;
        } else {
            return null;
        }
    }
}
