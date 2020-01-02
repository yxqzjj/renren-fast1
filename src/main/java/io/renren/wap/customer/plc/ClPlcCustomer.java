package io.renren.wap.customer.plc;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsClblockDaoImpl;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.dto.*;
import io.renren.wap.client.util.MsgCreateUtil;
import io.renren.wap.client.util.MsgSubstringUtil;
import io.renren.wap.constant.company.WuHanYouJiConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.WMSMessageLogConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.service.CreateXmlService;
import io.renren.wap.server.xml.constant.XmlInfoConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.server.xml.dto.node.LoadUnitAtIDDTO;
import io.renren.wap.server.xml.dto.node.item.control.ControlAreaDTO;
import io.renren.wap.server.xml.dto.node.item.control.ReceiverDTO;
import io.renren.wap.server.xml.dto.node.item.control.RefIdDTO;
import io.renren.wap.server.xml.dto.node.item.control.SenderDTO;
import io.renren.wap.server.xml.dto.node.item.data.LoadUnitAtIDDataDTO;
import io.renren.wap.server.xml.dto.node.item.data.location.LocationDetailDTO;
import io.renren.wap.service.WmsMessageLogService;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.service.warehouse.FinishWorkPlanInterface;
import io.renren.wap.service.warehouse.impl.FinishWorkPlanImpl;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;
import io.renren.wap.thread.callable.ResendLoadUnitAtIdCallable;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  17:24
 * @Version: V1.0.0
 **/
public class ClPlcCustomer extends AbstractPlcCustomer {

    public ClPlcCustomer(String plcName) {
        super(plcName);
    }

    @Override
    void doMsgChangeStationModeAckDTO(MsgChangeStationModeAckDTO msgChangeStationModeAckDTO) throws InterruptedException {
        String station = msgChangeStationModeAckDTO.getStation();
        String mode = msgChangeStationModeAckDTO.getMode();
        WcsStationmodeEntity stationMode = new WcsStationmodeEntity();
        stationMode.setName(station);
        stationMode.setMode(Integer.parseInt(mode));
        DbUtil.getStationModeDao().update(stationMode,new QueryWrapper<WcsStationmodeEntity>());
        EnvelopeDTO envelopeDTO = CreateXmlService.createTransportModeChangeReport(station, mode);
        XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, envelopeDTO);
        WmsMessageLogService wmsMessageLogService = new WmsMessageLogService();
        wmsMessageLogService.insertWMSMessageLog(envelopeDTO, WMSMessageLogConstant.TYPE_TRANSPORTMODECHANGEREPORT, WMSMessageLogConstant.STATUS_SEND);
    }

    @Override
    void doMsgMachineryStatusOrderAckDTO(MsgMachineryStatusOrderAckDTO msgMachineryStatusOrderAckDTO) {
        // 输送线待定
        BlockService blockService = new BlockService();
        List<MsgMachineryStatusOrderAckDTO.MachineStatus> machineStatusList = msgMachineryStatusOrderAckDTO.getMachineStatusList();
        for (MsgMachineryStatusOrderAckDTO.MachineStatus machineStatus : machineStatusList) {
            String machineName = machineStatus.getMachineName();
            String exceptionCode = machineStatus.getExceptionCode();
            blockService.updateBlockErrorCode(exceptionCode, DbUtil.getMLBlockDao(), machineName);
        }
    }

    @Override
    void doMsgDeleteDataAckDTO(MsgDeleteDataAckDTO msgDeleteDataAckDTO) {

    }

    @Override
    void doMsgConveyorLineDataReportDTO(MsgConveyorLineDataReportDTO msgConveyorLineDataReportDTO) throws InterruptedException {
        String station = msgConveyorLineDataReportDTO.getBlockNo();
        String msgMcKey = msgConveyorLineDataReportDTO.getMcKey();
        String blockName;
        if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
            blockName = station;
        } else {
            WcsMachineEntity machine1 = new WcsMachineEntity();
            machine1.setStationName(station);
            WcsMachineEntity machine = DbUtil.getMachineDao().selectOne((Wrapper<WcsMachineEntity>) new Object());
            blockName = machine.getBlockName();
        }
        String strIn="0001";
        if (strIn.equals(blockName)){
            if (SystemCache.AUTO_LOADUNITATID) {
                sendMsgToWms(msgConveyorLineDataReportDTO);
            }
        }else {
            updateLoadStatus(msgConveyorLineDataReportDTO);
            MsgConveyorLineDataReportAckDTO msgConveyorLineDataReportAckDTO = new MsgConveyorLineDataReportAckDTO();
            MsgCreateUtil.createMsgConveyorLineDataReportAckDTO(msgConveyorLineDataReportAckDTO, msgConveyorLineDataReportDTO);
            MsgQueueCache.addSendMsg(msgConveyorLineDataReportAckDTO);
            WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
            if (workPlan != null) {
                if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlan.getType()) {
                    WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                    String mcKey = clBlock.getMckey();
                    if (msgMcKey.equals(mcKey)) {
                        int resultInt = WcsClblockDaoImpl.getInstance().updateMcKeyAndLoad("", false, blockName);
                        Log4j2Util.getMsgCustomerLogger().info(String.format("站台：%s 50处理,出库完成修改载荷状态：%d：", mcKey, resultInt));
                        FinishWorkPlanInterface finishWorkPlan = new FinishWorkPlanImpl();
                        finishWorkPlan.finishOutStorage(workPlan);
                        BlockServiceImplFactory.blockServiceDoKey(blockName);
                    } else {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("输送线50处理处,消息中mckey与block状态中的值不一致，mcKey：%s，msgMcKey：%s：", mcKey, msgMcKey));
                    }
                }
            }
        }
    }

    /**
     * 修改载荷状态
     *
     * @param msgConveyorLineDataReportDTO 50消息
     * @author CalmLake
     * @date 2019/4/3 17:00
     */
    private void updateLoadStatus(MsgConveyorLineDataReportDTO msgConveyorLineDataReportDTO) {
        String station = msgConveyorLineDataReportDTO.getBlockNo();
        String load = msgConveyorLineDataReportDTO.getLoadStatus();
        String blockName;
        if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
            blockName = station;
        } else {
            WcsMachineEntity machine1 = new WcsMachineEntity();
            machine1.setStationName(station);
            WcsMachineEntity machine = DbUtil.getMachineDao().selectOne((Wrapper<WcsMachineEntity>) new Object());
            blockName = machine.getBlockName();
        }
        //String转Boolean.getBoolean(a);
        int result = WcsClblockDaoImpl.getInstance().updateCLBlockLoad(Boolean.getBoolean(load),blockName);
        Log4j2Util.getMsgCustomerLogger().info(String.format("block：%s ， 修改载荷状态结果：%d ，50载荷数据：%s", blockName, result, load));
    }

    /**
     * 和wms交互
     *
     * @param msgConveyorLineDataReportDTO 50消息
     * @author CalmLake
     * @date 2019/3/1 12:06
     */
    private void sendMsgToWms(MsgConveyorLineDataReportDTO msgConveyorLineDataReportDTO) throws InterruptedException {
        String blockName = msgConveyorLineDataReportDTO.getBlockNo();
        //  佳田货形 50 数据  1-高，2-低
        String height=msgConveyorLineDataReportDTO.getCargoHeight();
        String barcode = MsgSubstringUtil.getRealBarcodeString(msgConveyorLineDataReportDTO.getBarcode());
        MsgConveyorLineDataReportAckDTO msgConveyorLineDataReportAckDTO = new MsgConveyorLineDataReportAckDTO();
        MsgCreateUtil.createMsgConveyorLineDataReportAckDTO(msgConveyorLineDataReportAckDTO, msgConveyorLineDataReportDTO);
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        LoadUnitAtIDDTO loadUnitAtIDDTO = new LoadUnitAtIDDTO();
        ControlAreaDTO controlAreaDTO = new ControlAreaDTO();
        LoadUnitAtIDDataDTO loadUnitAtIDDataDTO = new LoadUnitAtIDDataDTO();
        SenderDTO senderDTO = new SenderDTO();
        ReceiverDTO receiverDTO = new ReceiverDTO();
        RefIdDTO refIdDTO = new RefIdDTO();
        LocationDetailDTO locationDetailDTO = new LocationDetailDTO();
        if (CompanyConstant.SYS_NAME_COMPANY_YOU_JI.equals(SystemCache.SYS_NAME_COMPANY)) {
            // 武汉有机项目 特殊站台做不同的处理
            if (WuHanYouJiConstant.STATION_1102.equals(blockName)) {
                // 1.请求校验
                loadUnitAtIDDataDTO.setScanDate(barcode);
                loadUnitAtIDDataDTO.setInformation(WuHanYouJiConstant.XML_INFORMATION_01);
            } else if (WuHanYouJiConstant.STATION_1301.equals(blockName)) {
                // 2.请求数据
                loadUnitAtIDDataDTO.setInformation(WuHanYouJiConstant.XML_INFORMATION_03);
            } else if (WuHanYouJiConstant.STATION_1202.equals(blockName)) {
                // 2.请求托盘
                loadUnitAtIDDataDTO.setInformation(WuHanYouJiConstant.XML_INFORMATION_02);
                MsgQueueCache.addSendMsg(msgConveyorLineDataReportAckDTO);
            } else {
                // 3.请求任务
                loadUnitAtIDDataDTO.setScanDate(barcode);
                loadUnitAtIDDataDTO.setErrorCode(XmlInfoConstant.XML_DEFAULT_00);
                MsgQueueCache.addSendMsg(msgConveyorLineDataReportAckDTO);
            }
        } else {
            MsgQueueCache.addSendMsg(msgConveyorLineDataReportAckDTO);
        }
        loadUnitAtIDDataDTO.setRouteChangeReguest(XmlInfoConstant.XML_DEFAULT_00);
        loadUnitAtIDDataDTO.setWeight(XmlInfoConstant.XML_DEFAULT_0);
        loadUnitAtIDDataDTO.setLoadType(height);
        loadUnitAtIDDataDTO.setErrorCode(XmlInfoConstant.XML_DEFAULT_00);
        loadUnitAtIDDataDTO.setInformation(XmlInfoConstant.XML_DEFAULT_00);
        loadUnitAtIDDataDTO.setScanDate(barcode);
        if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)){
            locationDetailDTO.setMha("1101");
        }else {
            locationDetailDTO.setMha(blockName);
        }
        senderDTO.setDivision(XmlInfoConstant.XML_WCS_NAME);
        receiverDTO.setDivision(XmlInfoConstant.XML_WMS_NAME);
        controlAreaDTO.setCreationDateTime(DateFormatUtil.dateToString(new Date()));
        refIdDTO.setRefId(UUID.randomUUID().toString());
        controlAreaDTO.setRefIdDTO(refIdDTO);
        controlAreaDTO.setSenderDTO(senderDTO);
        controlAreaDTO.setReceiverDTO(receiverDTO);
        loadUnitAtIDDataDTO.setLocationDetailDTO(locationDetailDTO);
        loadUnitAtIDDTO.setControlAreaDTO(controlAreaDTO);
        loadUnitAtIDDTO.setLoadUnitAtIDDataDTO(loadUnitAtIDDataDTO);
        envelopeDTO.setLoadUnitAtIDDTO(loadUnitAtIDDTO);
        XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, envelopeDTO);
        ResendLoadUnitAtIdCallable resendLoadUnitAtIdCallable = new ResendLoadUnitAtIdCallable(envelopeDTO);
        ThreadPoolServiceSingleton.getInstance().getExecutorServiceCallable().submit(resendLoadUnitAtIdCallable);
        WmsMessageLogService wmsMessageLogService = new WmsMessageLogService();
        wmsMessageLogService.insertWMSMessageLog("", barcode, 0, envelopeDTO, WMSMessageLogConstant.TYPE_LOADUNITATID, WMSMessageLogConstant.STATUS_SEND, refIdDTO.getRefId());
    }
}
