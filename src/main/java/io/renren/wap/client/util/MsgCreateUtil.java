package io.renren.wap.client.util;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.*;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/4/2  9:25
 * @Version: V1.0.0
 **/
public class MsgCreateUtil {

    public static String replaceSendMsgBlockName(String blockName) {
        if (CompanyConstant.SYS_NAME_COMPANY_YAN_SHI_QU.equals(SystemCache.SYS_NAME_COMPANY)) {
            if ("AL01".equals(blockName)) {
                blockName = "0002";
            }
        }
        return blockName;
    }

    public static MsgDTO replaceReceiveMsgBlockName(MsgDTO msgDTO) {
        if (CompanyConstant.SYS_NAME_COMPANY_YAN_SHI_QU.equals(SystemCache.SYS_NAME_COMPANY)) {
            String blockName;
            String station;
            String dock;
            if (msgDTO instanceof MsgCycleOrderAckDTO) {
                blockName = ((MsgCycleOrderAckDTO) msgDTO).getMachineName();
                station = ((MsgCycleOrderAckDTO) msgDTO).getStation();
                dock = ((MsgCycleOrderAckDTO) msgDTO).getDock();
                if ("0002".equals(blockName)) {
                    ((MsgCycleOrderAckDTO) msgDTO).setMachineName("AL01");
                    msgDTO.setPlcName("BL01");
                }
                if ("0002".equals(station)) {
                    ((MsgCycleOrderAckDTO) msgDTO).setStation("AL01");
                    msgDTO.setPlcName("BL01");
                }
                if ("0002".equals(dock)) {
                    ((MsgCycleOrderAckDTO) msgDTO).setDock("AL01");
                    msgDTO.setPlcName("BL01");
                }
            } else if (msgDTO instanceof MsgCycleOrderFinishReportDTO) {
                blockName = ((MsgCycleOrderFinishReportDTO) msgDTO).getMachineName();
                station = ((MsgCycleOrderFinishReportDTO) msgDTO).getStation();
                dock = ((MsgCycleOrderFinishReportDTO) msgDTO).getDock();
                if ("0002".equals(blockName)) {
                    ((MsgCycleOrderFinishReportDTO) msgDTO).setMachineName("AL01");
                    msgDTO.setPlcName("BL01");
                }
                if ("0002".equals(station)) {
                    ((MsgCycleOrderFinishReportDTO) msgDTO).setStation("AL01");
                    msgDTO.setPlcName("BL01");
                }
                if ("0002".equals(dock)) {
                    ((MsgCycleOrderFinishReportDTO) msgDTO).setDock("AL01");
                    msgDTO.setPlcName("BL01");
                }
            }
        }
        return msgDTO;
    }

    /**
     * 制作 MsgConveyorLineDataReportAckDTO 55 消息
     *
     * @param msgConveyorLineDataReportDTO    50消息
     * @param msgConveyorLineDataReportAckDTO 55消息
     * @author CalmLake
     * @date 2019/3/1 13:49
     */
    public static void createMsgConveyorLineDataReportAckDTO(MsgConveyorLineDataReportAckDTO msgConveyorLineDataReportAckDTO, MsgConveyorLineDataReportDTO msgConveyorLineDataReportDTO) {
        msgConveyorLineDataReportAckDTO.setBarcode(msgConveyorLineDataReportDTO.getBarcode());
        msgConveyorLineDataReportAckDTO.setBlockNo(msgConveyorLineDataReportDTO.getBlockNo());
        msgConveyorLineDataReportAckDTO.setCargoHeight(msgConveyorLineDataReportDTO.getCargoHeight());
        msgConveyorLineDataReportAckDTO.setCargoWidth(msgConveyorLineDataReportDTO.getCargoWidth());
        msgConveyorLineDataReportAckDTO.setDataNum(msgConveyorLineDataReportDTO.getDataNum());
        msgConveyorLineDataReportAckDTO.setLoadStatus(msgConveyorLineDataReportDTO.getLoadStatus());
        msgConveyorLineDataReportAckDTO.setMcKey(msgConveyorLineDataReportDTO.getMcKey());
        msgConveyorLineDataReportAckDTO.setStorageNum(msgConveyorLineDataReportDTO.getStorageNum());
        msgConveyorLineDataReportAckDTO.setPlcName(msgConveyorLineDataReportDTO.getPlcName());
        msgConveyorLineDataReportAckDTO.setBcc(BccUtil.getBcc(msgConveyorLineDataReportAckDTO.getData()));
        msgConveyorLineDataReportAckDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CONVEYORLINE_DATA_REPORT_ACK);
        msgConveyorLineDataReportAckDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgConveyorLineDataReportAckDTO.setReSend(MsgConstant.RESEND_SEND);
        msgConveyorLineDataReportAckDTO.setMessageNumber(msgConveyorLineDataReportDTO.getMessageNumber());
    }

    /**
     * 制作 MsgConveyorLineDataReportAckDTO 55 消息
     *
     * @param msgConveyorLineDataReportAckDTO 55 对象
     * @param barcode                         托盘号
     * @param blockNo                         数据block名称（站台号）
     * @param cargoHeight                     货物高
     * @param cargoWidth                      货物宽
     * @param dataNum                         数据数量
     * @param loadStatus                      载荷状态
     * @param mcKey                           任务标识
     * @param storageNum                      载荷数量
     * @param weight                          重量
     * @param plcName                         plc名称
     * @author CalmLake
     * @date 2019/4/2 9:48
     */
    public static void createMsgConveyorLineDataReportAckDTO(MsgConveyorLineDataReportAckDTO msgConveyorLineDataReportAckDTO, String barcode, String blockNo, String cargoHeight, String cargoWidth, String dataNum, String loadStatus, String mcKey, String storageNum, String weight, String plcName) {
        msgConveyorLineDataReportAckDTO.setBarcode(barcode);
        msgConveyorLineDataReportAckDTO.setBlockNo(blockNo);
        msgConveyorLineDataReportAckDTO.setCargoHeight(cargoHeight);
        msgConveyorLineDataReportAckDTO.setCargoWidth(cargoWidth);
        msgConveyorLineDataReportAckDTO.setDataNum(dataNum);
        msgConveyorLineDataReportAckDTO.setLoadStatus(loadStatus);
        msgConveyorLineDataReportAckDTO.setMcKey(mcKey);
        msgConveyorLineDataReportAckDTO.setStorageNum(storageNum);
        msgConveyorLineDataReportAckDTO.setWeight(weight);
        msgConveyorLineDataReportAckDTO.setPlcName(plcName);
        msgConveyorLineDataReportAckDTO.setBcc(BccUtil.getBcc(msgConveyorLineDataReportAckDTO.getData()));
        msgConveyorLineDataReportAckDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CONVEYORLINE_DATA_REPORT_ACK);
        msgConveyorLineDataReportAckDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgConveyorLineDataReportAckDTO.setReSend(MsgConstant.RESEND_SEND);
        msgConveyorLineDataReportAckDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
    }

    /**
     * 创建设备启动消息
     *
     * @param plcName plc名称
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/5/5 11:47
     */
    public static MsgDTO createMsgStartMachineryDTO(String plcName) {
        MsgStartMachineryDTO msgStartMachineryDTO = new MsgStartMachineryDTO();
        List<String> stringList = new ArrayList<>();
        if (plcName.contains(MachineConstant.TYPE_PLC_NAME_BL)) {
            stringList.add("0000");
        } else {
            stringList.add(plcName);
        }
        msgStartMachineryDTO.setMachineNameList(stringList);
        msgStartMachineryDTO.setDataNum("001");
        msgStartMachineryDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_START_MACHINERY);
        msgStartMachineryDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgStartMachineryDTO.setReSend(MsgConstant.RESEND_SEND);
        msgStartMachineryDTO.setBcc(BccUtil.getBcc(msgStartMachineryDTO.getData()));
        msgStartMachineryDTO.setPlcName(plcName);
        msgStartMachineryDTO.setMessageNumber(MsgCycleOrderConstant.DEFAULT_ZERO);
        return msgStartMachineryDTO;
    }
}
