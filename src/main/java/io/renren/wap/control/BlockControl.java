package io.renren.wap.control;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.WcsCrossrouteDao;
import io.renren.modules.generator.dao.impl.*;
import io.renren.modules.generator.entity.*;
import io.renren.wap.block.status.AbstractBlockStatus;
import io.renren.wap.block.status.BlockStatus;
import io.renren.wap.block.status.BlockStatusInterface;
import io.renren.wap.block.status.BlockStatusKeyImpl;
import io.renren.wap.cache.BlockCache;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgMachineryStatusOrderAskDTO;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.client.util.MessageDetailUtil;
import io.renren.wap.constant.DatePatternConstant;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.wap.dao.*;
import io.renren.wap.entity.constant.*;
import io.renren.wap.factory.BlockDaoFactory;
import io.renren.wap.factory.BlockDaoImpl;
import io.renren.wap.service.ChargeService;
import io.renren.wap.service.TransferTasksService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.service.charge.ChargeImpl;
import io.renren.wap.service.charge.ChargeInterface;
import io.renren.wap.util.ChargeLocationUtil;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * block操作
 *
 * @Author: CalmLake
 * @Date: 2019/2/22  11:23
 * @Version: V1.0.0
 **/
@Controller
@RequestMapping("blockControl")
public class BlockControl {
    @Resource(name = "ChargeDao")
    private ChargeDao chargeDao;
    @Resource(name = "ErrorCodeDao")
    private ErrorCodeDao errorCodeDao;
    @Resource(name = "PlcConfigDao")
    private PlcConfigDao plcConfigDao;
    @Resource(name = "CrossRouteDao")
    private CrossRouteDao crossRouteDao;
    @Resource(name = "MachineDao")
    private MachineDao machineDao;
    @Resource(name = "WorkPlanDao")
    private WorkPlanDao workPlanDao;

    @RequestMapping("finishCycleCommand")
    @ResponseBody
    public JSONObject finishCycleCommand(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String msg;
        boolean result;
        String getCar = "getCar";
        String name = request.getParameter("name");
        String scName = request.getParameter("scName");
        String mcKey = request.getParameter("mcKey");
        String loadStatus = request.getParameter("loadStatus");
        String type = request.getParameter("type");
        WcsMachineEntity machine = MachineCache.getMachine(name);
        BlockCache.addString(name, null);
        AbstractBlockStatus abstractBlockStatus = new BlockStatus(machine);
        abstractBlockStatus.setMachineValues();
        Block block = abstractBlockStatus.getBlock();
        String blockMcKey = block.getMckey();
        String blockAppointmentMcKey = block.getAppointmentMckey();
        if (StringUtils.isEmpty(mcKey)) {
            msg = "mckey不能为空，请大人睁大眼睛仔细检查！仔细检查！仔细检查！";
            result = false;
        } else {
            if (mcKey.equals(blockMcKey) || mcKey.equals(blockAppointmentMcKey)) {
                if (getCar.equals(type)) {
                    try {
                        TransferTasksService tasksService = new TransferTasksService();
                        tasksService.mlGetCarFinishTransferTasks(mcKey, name, scName, loadStatus);
                        msg = "任务分配执行完成！";
                        result = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg = "出错了！打119！" + e.getMessage();
                        result = false;
                    }
                } else {
                    msg = "未解析的动作类型！打110！";
                    result = false;
                }
            } else {
                msg = "mckey与设备执行任务不一致，请大人睁大眼睛仔细检查！仔细检查！仔细检查！";
                result = false;
            }
        }
        jsonObject.put("result", result);
        jsonObject.put("msg", msg);
        return jsonObject;
    }


   /* @RequestMapping("updateCrossRoute")
    @ResponseBody
    public JSONObject updateCrossRoute(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String runBlockName = request.getParameter("runBlockName");
        String loadNum = request.getParameter("loadNum");
        String mode = request.getParameter("mode");
        String msg;
        boolean result;
        WcsCrossrouteEntity crossRoute = WcsCrossrouteDao.selectByPrimaryKey(runBlockName);
        crossRoute.setMode(Integer.parseInt(mode));
        crossRoute.setLoadNum(Integer.parseInt(loadNum));
        crossRouteDao.updateByPrimaryKey(crossRoute);
        msg = "交叉路径信息修改成功！";
        result = true;
        jsonObject.put("result", result);
        jsonObject.put("msg", msg);
        Log4j2Util.getOperationLog().info(String.format("%s,交叉路径信息修改操作，结果：%s", runBlockName, jsonObject.toJSONString()));
        return jsonObject;
    }*/


    /**
     * 查找交叉路径信息
     *
     * @param request 请求参数
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/9/3 21:34
     */
    @RequestMapping("selectCrossRoute")
    @ResponseBody
    public JSONObject selectCrossRoute(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String blockName = request.getParameter("blockName");
        String msg;
        WcsCrossrouteEntity crossRoute = crossRouteDao.selectByPrimaryKey(blockName);
        jsonObject.put("runBlockName", crossRoute.getRunBlockName());
        jsonObject.put("mode", crossRoute.getMode());
        jsonObject.put("maxLoadNum", crossRoute.getMaxLoadNum());
        jsonObject.put("loadNum", crossRoute.getLoadNum());
        msg = "信息查找完成！";
        jsonObject.put("result", true);
        jsonObject.put("msg", msg);
        Log4j2Util.getOperationLog().info(String.format("%s,查找交叉路径信息，结果：%s", blockName, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 重新分配交叉路径任务
     *
     * @param request 请求参数
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/9/3 21:34
     */
    @RequestMapping("signalRunBlock")
    @ResponseBody
    public JSONObject signalRunBlock(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String blockName = request.getParameter("blockName");
        String msg;
        boolean result;
        LockCache.getValue(blockName).signal();
        msg = "任务重新分配成功！";
        result = true;
        jsonObject.put("result", result);
        jsonObject.put("msg", msg);
        Log4j2Util.getOperationLog().info(String.format("%s,重新分配交叉路径任务，结果：%s", blockName, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 获取交叉路径信息
     *
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/9/3 21:34
     */
    @RequestMapping("getCrossRoute")
    @ResponseBody
    public JSONArray getCrossRoute() {
        JSONArray jsonArray = new JSONArray();
        List<WcsCrossrouteEntity> crossRouteList = crossRouteDao.getListAll();
        for (WcsCrossrouteEntity crossRoute : crossRouteList) {
            JSONObject jsonObject = new JSONObject();
            String mode;
            int modeByte = crossRoute.getMode();
            String runBlockName = crossRoute.getRunBlockName();
            WcsMachineEntity machine = machineDao.selectPlcName(runBlockName);
            if (CrossRouteConstant.MODE_DEFAULT == modeByte) {
                mode = "默认";
            } else if (CrossRouteConstant.MODE_PUT_IN == modeByte) {
                mode = "入库";
            } else if (CrossRouteConstant.MODE_OUT_PUT == modeByte) {
                mode = "出库";
            } else {
                mode = "未知";
            }
            String warehouse;
            switch (machine.getWarehouseNo()) {
                case 1:
                    warehouse = "1-1库";
                    break;
                case 2:
                    warehouse = "1-2库";
                    break;
                case 3:
                    warehouse = "2-1库";
                    break;
                case 4:
                    warehouse = "2-2库";
                    break;
                default:
                    warehouse = "什么鬼";
                    break;
            }
            jsonObject.put("loadNum", crossRoute.getLoadNum());
            jsonObject.put("maxLoadNum", crossRoute.getMaxLoadNum());
            jsonObject.put("mode", mode);
            jsonObject.put("runBlockName", runBlockName);
            jsonObject.put("warehouse", warehouse);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    /**
     * 清除设备异常
     *
     * @param request 请求参数
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/9/3 21:34
     */
    @RequestMapping("clearError")
    @ResponseBody
    public JSONObject clearError(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String blockName = request.getParameter("blockName");
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        String runBlockName = machine.getReserved1();
        String plcName = machine.getPlcName();
        String msg;
        boolean result;
        MsgMachineryStatusOrderAskDTO msgMachineryStatusOrderAskDTO = new MsgMachineryStatusOrderAskDTO();
        msgMachineryStatusOrderAskDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgMachineryStatusOrderAskDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_MACHINERY_STATUS_ORDER_ASK);
        msgMachineryStatusOrderAskDTO.setReSend(MsgConstant.RESEND_SEND);
        msgMachineryStatusOrderAskDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgMachineryStatusOrderAskDTO.setPlcName(plcName);
        msgMachineryStatusOrderAskDTO.setMachineName(runBlockName);
        msgMachineryStatusOrderAskDTO.setStatus(MsgMachineryStatusOrderAskDTO.STATUS_REQUEST_EXCEPTION_HANDING);
        msgMachineryStatusOrderAskDTO.setBcc(BccUtil.getBcc(msgMachineryStatusOrderAskDTO.getData()));
        try {
            MsgQueueCache.addSendMsg(msgMachineryStatusOrderAskDTO);
            msg = "消息制作成功！";
            result = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            msg = "消息制作失败！" + e.getMessage();
            result = true;
        }
        jsonObject.put("result", result);
        jsonObject.put("msg", msg);
        Log4j2Util.getOperationLog().info(String.format("%s,清除故障操作，结果：%s", blockName, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 查看故障码对应的故障信息
     *
     * @param request 请求数据
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/6/21 9:55
     */
    @RequestMapping("lookErrorCodeDetail")
    @ResponseBody
    public JSONObject lookErrorCodeDetail(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String msg;
        boolean result;
        //  设备类型
        String type = request.getParameter("type");
        //  故障代码
        String errorCodeString = request.getParameter("errorCode");
        WcsErrorcodeEntity errorCode = errorCodeDao.getErrorCodeByTypeAndErrorCode(Byte.parseByte(type), errorCodeString);
        if (errorCode != null) {
            result = true;
            msg = errorCode.getErrorDetail();
        } else {
            result = false;
            msg = "字典中无对应数据！";
        }
        jsonObject.put("result", result);
        jsonObject.put("errorCode", errorCodeString);
        jsonObject.put("msg", msg);
        return jsonObject;
    }

    /**
     * 更换车辆（主备车切换）
     *
     * @param request 请求数据
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/6/21 9:55
     */
    @RequestMapping("changeCar")
    @ResponseBody
    public JSONObject changeCar(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String msg;
        boolean result;
        String name = null;
        try {
            name = request.getParameter("name");
            if (name.contains(MachineConstant.TYPE_ML)) {
                WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(name);
                if (mlBlock.getIsStandbyCar()) {
                    //  切回原配
                    WcsMlblockDaoImpl.getInstance().updateIsStandbyCarByName(name, false, "");
                    String standbyCarBlockName = mlBlock.getStandbyCarBlockName();
                    WcsScblockDaoImpl.getInstance().updateIsUseByName(standbyCarBlockName, false);
                    ChargeLocationUtil.getInstance().recycleLocation(standbyCarBlockName);
                    chargeDao.deleteByPrimaryKey(standbyCarBlockName);
                    WcsScblockDaoImpl.getInstance().updateIsUseByName(mlBlock.getBingScBlockName(), true);
                    WcsScblockDaoImpl.getInstance().updateStatus(mlBlock.getBingScBlockName(), BlockConstant.STATUS_RUNNING);
                } else {
                    //  使用备车
                    //  2.2 是否有备车
                    List<WcsScblockEntity> scBlockList = WcsScblockDaoImpl.getInstance().getScBlockListByStandbyCar(true);
                    if (scBlockList.size() > 0) {
                        //  3.备车是否可用
                        for (WcsScblockEntity scBlock : scBlockList) {
                            if (!scBlock.getIsUse()) {
                                WcsMlblockDaoImpl.getInstance().updateIsStandbyCarByName(name, true, scBlock.getName());
                                WcsScblockDaoImpl.getInstance().updateIsUseByName(scBlock.getName(), true);
                                WcsScblockDaoImpl.getInstance().updateStatus(scBlock.getName(), BlockConstant.STATUS_RUNNING);
                                chargeDao.deleteByPrimaryKey(scBlock.getName());
                                ChargeService chargeService = new ChargeService();
                                chargeService.createChargeRoute(mlBlock.getBingScBlockName(), scBlock.getName());
                                WcsScblockDaoImpl.getInstance().updateIsUseByName(mlBlock.getBingScBlockName(), false);
                                ChargeLocationUtil.getInstance().recycleLocation(mlBlock.getBingScBlockName());
                            }
                        }
                    }
                }
                msg = "切换成功";
                result = true;
            } else {
                msg = "修改载车状态失败！未解析设备名称";
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "异常：" + e.getMessage();
            result = false;
        }
        jsonObject.put("msg", msg);
        jsonObject.put("result", result);
        Log4j2Util.getOperationLog().info(String.format("%s,更换主备车操作，结果：%s", name, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 获取寄宿设备名称
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/4/19 11:56
     */
    @RequestMapping("getMBlockNameList")
    @ResponseBody
    public JSONArray getMBlockNameList() {
        JSONArray jsonArray = new JSONArray();
        try {
            List<WcsMlblockEntity> mlBlockName = DbUtil.getMLBlockDao().selectList(new QueryWrapper<WcsMlblockEntity>().select("Name"));
            for (WcsMlblockEntity wcsMlblockEntity : mlBlockName) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mBlockName", wcsMlblockEntity.getName());
                jsonArray.add(jsonObject);
            }
            List<WcsMcblockEntity> mcBlockName = DbUtil.getMCBlockDao().selectList(new QueryWrapper<WcsMcblockEntity>().select("Name"));
            for (WcsMcblockEntity wcsMcblockEntity : mcBlockName) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mBlockName", wcsMcblockEntity.getName());
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 获取穿梭子车名称
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/4/19 11:56
     */
    @RequestMapping("getScBlockNameList")
    @ResponseBody
    public JSONArray getScBlockNameList() {
        JSONArray jsonArray = new JSONArray();
        try {
            List<WcsScblockEntity> scBlockName = DbUtil.getSCBlockDao().selectList(new QueryWrapper<WcsScblockEntity>().select("Name"));
            for (WcsScblockEntity wcsScblockEntity : scBlockName) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("scBlockName", wcsScblockEntity.getName());
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 修改载车 （堆垛机/母车）
     *
     * @param request web数据
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/3/19 10:05
     */
    @RequestMapping("updateScStatus")
    @ResponseBody
    public JSONObject updateScStatus(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String scStatus = request.getParameter("scStatus");
        try {
            boolean result;
            int num = WcsScblockDaoImpl.getInstance().updateStatus(name, scStatus);
            if (num > 0) {
                jsonObject.put("msg", "修改穿梭车状态成功！");
                result = true;
            } else {
                jsonObject.put("msg", "修改穿梭车状态失败！");
                result = false;
            }
            jsonObject.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "修改穿梭车状态失败！" + e.getMessage());
        }
        Log4j2Util.getOperationLog().info(String.format("%s,修改穿梭车状态操作，状态：%s，结果：%s", name, scStatus, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 修改载车 （堆垛机/母车）
     *
     * @param request web数据
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/3/19 10:05
     */
    @RequestMapping("updateHostBlockName")
    @ResponseBody
    public JSONObject updateHostBlockName(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String mName = request.getParameter("mName");
        try {
            String machineType;
            machineType = "sc";
            boolean result;
            int num = WcsScblockDaoImpl.getInstance().updateHostName(name, mName);
            if (num > 0) {
                jsonObject.put("msg", "修改寄宿设备名称成功！");
                jsonObject.put("machineType", machineType);
                result = true;
            } else {
                jsonObject.put("msg", "修改寄宿设备名称失败！");
                result = false;
            }
            jsonObject.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "修改寄宿设备名称失败！" + e.getMessage());
        }
        Log4j2Util.getOperationLog().info(String.format("%s,修改宿主设备操作，宿主：%s，结果：%s", name, mName, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 修改载车 （堆垛机/母车）
     *
     * @param request web数据
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/3/19 10:05
     */
    @RequestMapping("updateLoadCar")
    @ResponseBody
    public JSONObject updateLoadCar(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String scName = request.getParameter("scName");
        try {
            String machineType;
            boolean result;
            if (name.contains(MachineConstant.TYPE_MC)) {
                machineType = "mc";
                WcsMcblockDaoImpl.getInstance().updateScBlockName(name, scName);
                result = true;
            } else if (name.contains(MachineConstant.TYPE_ML)) {
                machineType = "ml";
                WcsMlblockDaoImpl.getInstance().updateScBlockName(name, scName);
                result = true;
            } else {
                machineType = "";
                result = false;
            }
            if (result) {
                jsonObject.put("msg", "修改载车状态成功！");
                jsonObject.put("machineType", machineType);
            } else {
                jsonObject.put("msg", "修改载车状态失败！");
            }
            jsonObject.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "修改载车状态失败！" + e.getMessage());
        }
        Log4j2Util.getOperationLog().info(String.format("%s,修改设备载车操作，载车名称：%s，结果：%s", name, scName, jsonObject.toJSONString()));
        return jsonObject;
    }

    /**
     * 使有任务标识的设备继续工作
     *
     * @param request 请求参数
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/3/15 14:44
     */
    @RequestMapping("doWork")
    @ResponseBody
    public JSONObject doWork(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            String name = request.getParameter("name");
            WcsMachineEntity machine = MachineCache.getMachine(name);
            String machineType;
            if (name.contains(MachineConstant.TYPE_SC)) {
                machineType = "sc";
                WcsScblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else if (name.contains(MachineConstant.TYPE_MC)) {
                machineType = "mc";
                WcsMcblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else if (name.contains(MachineConstant.TYPE_ML)) {
                machineType = "ml";
                WcsMlblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else if (name.contains(MachineConstant.TYPE_RGV)) {
                machineType = "rgv";
                WcsRgvblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else if (name.contains(MachineConstant.TYPE_AL)) {
                machineType = "al";
                WcsAlblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else {
                machineType = "cl";
                WcsClblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            }
            BlockCache.addString(name, null);
            AbstractBlockStatus abstractBlockStatus = new BlockStatus(machine);
            abstractBlockStatus.setMachineValues();
            Block block = abstractBlockStatus.getBlock();
            BlockStatusInterface blockStatusInterface = new BlockStatusKeyImpl(block);
            String keyType = blockStatusInterface.judgeMachineStatus();
            if (BlockConstant.KEY_EMPTY_STRING.equals(keyType) && machine.getTaskFlag()) {
                LockCache.getValue(name).signal();
                jsonObject.put("msg", "重新获取任务！");
            } else {
                if (BlockConstant.APPOINTMENT_MCKEY_NOT_EMPTY_STRING.equals(keyType)) {
                    BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
                    BlockDao blockDao = blockDaoFactory.getBlockDao(machine);
                    blockDao.updateAppointmentMcKeyReserved1ByName(name, "", "");
                    blockDao.updateMcKeyByName(block.getAppointmentMckey(), block.getReserved1(), name);
                } else if (BlockConstant.KEY_NOT_EMPTY_STRING.equals(keyType)) {
                    String mcKey = block.getMckey();
                    String appointmentMcKey = block.getAppointmentMckey();
                    WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
                    if (workPlan != null) {
                        if (WorkPlanConstant.STATUS_CANCEL==workPlan.getStatus() || WorkPlanConstant.STATUS_FINISH==workPlan.getStatus()) {
                            BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
                            BlockDao blockDao = blockDaoFactory.getBlockDao(machine);
                            blockDao.updateAppointmentMcKeyReserved1ByName(name, "", "");
                            blockDao.updateMcKeyByName(appointmentMcKey, block.getReserved1(), name);
                        }
                    } else {
                        BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
                        BlockDao blockDao = blockDaoFactory.getBlockDao(machine);
                        blockDao.updateAppointmentMcKeyReserved1ByName(name, "", "");
                        blockDao.updateMcKeyByName(appointmentMcKey, block.getReserved1(), name);
                    }
                }
                BlockServiceImplFactory.blockServiceDoKey(name);
                jsonObject.put("msg", "任务下发成功！");
            }
            jsonObject.put("result", true);
            jsonObject.put("machineType", machineType);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "任务下发失败！" + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 修改设备的载荷状态
     *
     * @param request 请求参数
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/3/15 14:44
     */
    @RequestMapping("updateLoad")
    @ResponseBody
    public JSONObject updateLoad(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        try {
            int result;
            boolean isLoad;
            String machineType;
            if (name.contains(MachineConstant.TYPE_SC)) {
                machineType = "sc";
                isLoad = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(name).getIsLoad();
                result = WcsScblockDaoImpl.getInstance().updateLoad(name, !isLoad);
            } else if (name.contains(MachineConstant.TYPE_MC)) {
                machineType = "mc";
                isLoad = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(name).getIsLoad();
                result = WcsMcblockDaoImpl.getInstance().updateMcBlockLoad(!isLoad,name);
            } else if (name.contains(MachineConstant.TYPE_ML)) {
                machineType = "ml";
                isLoad = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(name).getIsLoad();
                result = WcsMlblockDaoImpl.getInstance().updateMlBlockLoad(!isLoad,name);
            } else if (name.contains(MachineConstant.TYPE_RGV)) {
                machineType = "rgv";
                isLoad = WcsRgvblockDaoImpl.getInstance().selectByPrimaryKey(name).getIsLoad();
                result = WcsRgvblockDaoImpl.getInstance().updateLoad(name, !isLoad);
            } else if (name.contains(MachineConstant.TYPE_AL)) {
                machineType = "al";
                isLoad = WcsAlblockDaoImpl.getInstance().selectByPrimaryKey(name).getIsLoad();
                result = WcsAlblockDaoImpl.getInstance().updateALBlockLoad(!isLoad,name);
            } else {
                machineType = "cl";
                isLoad = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(name).getIsLoad();
                result = WcsClblockDaoImpl.getInstance().updateCLBlockLoad(!isLoad,name);
            }
            if (result > 0) {
                jsonObject.put("result", true);
                jsonObject.put("msg", "修改载荷成功！");
                jsonObject.put("machineType", machineType);
            } else {
                jsonObject.put("result", false);
                jsonObject.put("msg", "修改载荷失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "修改载荷失败！" + e.getMessage());
        }
        Log4j2Util.getOperationLog().info(String.format("%s,修改设备载荷状态，结果：%s", name, jsonObject.toJSONString()));
        return jsonObject;
    }

    @RequestMapping("updateCommand")
    @ResponseBody
    public JSONObject updateCommand(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        try {
            int result;
            String machineType;
            if (name.contains(MachineConstant.TYPE_SC)) {
                machineType = "sc";
                result = WcsScblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else if (name.contains(MachineConstant.TYPE_MC)) {
                machineType = "mc";
                result = WcsMcblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else if (name.contains(MachineConstant.TYPE_ML)) {
                machineType = "ml";
                result = WcsMlblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else if (name.contains(MachineConstant.TYPE_RGV)) {
                machineType = "rgv";
                result = WcsRgvblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else if (name.contains(MachineConstant.TYPE_AL)) {
                machineType = "al";
                result = WcsAlblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            } else {
                machineType = "cl";
                result = WcsClblockDaoImpl.getInstance().updateCommandByPrimaryKey(name, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
            }
            BlockCache.addString(name, null);
            if (result > 0) {
                jsonObject.put("result", true);
                jsonObject.put("msg", "恢复成功！");
                jsonObject.put("machineType", machineType);
            } else {
                jsonObject.put("result", false);
                jsonObject.put("msg", "恢复失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "恢复失败！" + e.getMessage());
        }
        Log4j2Util.getOperationLog().info(String.format("%s,恢复设备动作状态，结果：%s", name, jsonObject.toJSONString()));
        return jsonObject;
    }

    @RequestMapping("chargeStart")
    @ResponseBody
    public JSONObject chargeStart(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String machineType = "sc";
        try {
            ChargeInterface chargeInterface = new ChargeImpl();
            chargeInterface.startCharge(name);
            jsonObject.put("result", true);
            jsonObject.put("msg", "充电开始指令下发成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "夭寿啦！充电开始指令出现异常！");
        }
        jsonObject.put("machineType", machineType);
        return jsonObject;
    }

    @RequestMapping("chargeFinish")
    @ResponseBody
    public JSONObject chargeFinish(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String machineType = null;
        try {
            if (SystemCache.STANDBY_CAR_SWITCH) {
                jsonObject.put("result", false);
                jsonObject.put("msg", "备车逻辑开启不能手动生成充电完成任务!");
            } else {
                WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(name);
                machineType = "sc";
                if (Integer.parseInt(scBlock.getKwh()) > SystemCache.SC_WORK_MIN_KWH) {
                    ChargeInterface chargeInterface = new ChargeImpl();
                    chargeInterface.finishCharge(name);
                    jsonObject.put("result", true);
                    jsonObject.put("msg", "充电完成指令下发成功");
                } else {
                    jsonObject.put("result", false);
                    jsonObject.put("msg", "电量未达到指定数值!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "夭寿啦！充电完成指令出现异常！");
        }
        jsonObject.put("machineType", machineType);
        return jsonObject;
    }

   /* @RequestMapping("clearKey")
    @ResponseBody
    public JSONObject clearKey(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        try {
            int typeInt = Integer.parseInt(type);
            int result = 0;
            String machineType = "";
            String msg = "";
            WcsMachineEntity machine = MachineCache.getMachine(name);
            AbstractBlockStatus blockStatus = new BlockStatus(machine);
            blockStatus.setMachineValues();
            String mckey = blockStatus.getBlock().getMckey();
            String appointmentMcKey = blockStatus.getBlock().getAppointmentMckey();
            String key;
            if (typeInt == 1) {
                key = mckey;
            } else {
                key = appointmentMcKey;
            }
            WcsWorkplanEntity workPlan = workPlanDao.selectByMcKey(key);
            if (workPlan != null) {
                if (WorkPlanConstant.STATUS_WAIT==workPlan.getType() || WorkPlanConstant.STATUS_WORKING==workPlan.getType()) {
                    msg = "清除失败，该任务正在执行中，请确认该任务已经完成！";
                    Log4j2Util.getOperationLog().info(String.format("%s,类型：%s，key:%s,清除设备任务，结果：%s", name, type, key, jsonObject.toJSONString()));
                    result = -1;
                }
            } else {
                if (name.contains(MachineConstant.TYPE_SC)) {
                    machineType = "sc";
                    if (typeInt == 1) {
                        result = WcsScblockDaoImpl.getInstance().updateMcKey("", name);
                    } else {
                        result = WcsScblockDaoImpl.getInstance().updateAppointmentMcKey("", name);
                    }
                } else if (name.contains(MachineConstant.TYPE_MC)) {
                    machineType = "mc";
                    if (typeInt == 1) {
                        result = WcsMcblockDaoImpl.getInstance().updateMcKey("", name);
                    } else {
                        result = WcsMcblockDaoImpl.getInstance().updateAppointmentMcKey("", name);
                    }
                    LockCache.getValue(name).signal();
                } else if (name.contains(MachineConstant.TYPE_ML)) {
                    machineType = "ml";
                    if (typeInt == 1) {
                        result = WcsMlblockDaoImpl.getInstance().updateMcKey("", name);
                    } else {
                        result = WcsMlblockDaoImpl.getInstance().updateAppointmentMcKey("", name);
                    }
                    LockCache.getValue(name).signal();
                } else if (name.contains(MachineConstant.TYPE_RGV)) {
                    machineType = "rgv";
                    if (typeInt == 1) {
                        result = WcsRgvblockDaoImpl.getInstance().updateMcKey("", name);
                    } else {
                        result = WcsRgvblockDaoImpl.getInstance().updateAppointmentMcKey("", name);
                    }
                } else if (name.contains(MachineConstant.TYPE_AL)) {
                    machineType = "al";
                    if (typeInt == 1) {
                        result = WcsAlblockDaoImpl.getInstance().updateMcKey("", name);
                    } else {
                        result = WcsAlblockDaoImpl.getInstance().updateAppointmentMcKey("", name);
                    }
                } else {
                    machineType = "cl";
                    if (typeInt == 1) {
                        result = WcsClblockDaoImpl.getInstance().updateMcKey("", name);
                    } else {
                        result = WcsClblockDaoImpl.getInstance().updateAppointmentMcKey("", name);
                    }
                    WcsClblockDaoImpl.getInstance().updateCLBlockLoad(false,name);
                }
                BlockCache.addString(name, null);
            }
            if (result > 0) {
                jsonObject.put("result", true);
                jsonObject.put("msg", "清除成功！");
                jsonObject.put("machineType", machineType);
            } else {
                jsonObject.put("result", false);
                jsonObject.put("msg", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            jsonObject.put("msg", "清除失败！" + e.getMessage());
        }
        Log4j2Util.getOperationLog().info(String.format("%s,类型：%s，清除设备任务，结果：%s", name, type, jsonObject.toJSONString()));
        return jsonObject;
    }*/

    /**
     * 获取升降机block列表
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/22 13:58
     */
    @RequestMapping("getAlBlockList")
    @ResponseBody
    public JSONArray getAlBlockList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsAlblockEntity> alBlockList = DbUtil.getALBlockDao().selectList(new QueryWrapper<WcsAlblockEntity>());
        for (WcsAlblockEntity alBlock : alBlockList) {
            WcsPlcconfigEntity plcConfig = plcConfigDao.selectByPrimaryKey(alBlock.getName());
            String socketStatus = PlcConfigConstant.getStatusString(plcConfig.getStatus());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name", alBlock.getName().trim());
            jsonObject.put("McKey", alBlock.getMckey());
            jsonObject.put("Appointment_McKey", alBlock.getAppointmentMckey());
            jsonObject.put("Command", alBlock.getCommand());
            jsonObject.put("Error_Code", alBlock.getErrorCode());
            jsonObject.put("Status", alBlock.getStatus());
            jsonObject.put("socketStatus", socketStatus);
            jsonObject.put("Is_Load", alBlock.getIsLoad());
            jsonObject.put("With_Work_Block_Name", alBlock.getWithWorkBlockName());
            jsonObject.put("Berth_Block_Name", alBlock.getBerthBlockName());
            jsonObject.put("Tier", alBlock.getTier());
            jsonObject.put("Reserved1", alBlock.getReserved1());
            jsonObject.put("cycleCommand", BlockCache.getString(alBlock.getName()) == null ? "" : MessageDetailUtil.getCycleCommandDetail(BlockCache.getString(alBlock.getName())));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取RGV block列表
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/22 13:58
     */
    @RequestMapping("getRgvBlockList")
    @ResponseBody
    public JSONArray getRgvBlockList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsRgvblockEntity> rgvBlockList = DbUtil.getRGVBlockDao().selectList(new QueryWrapper<>());
        for (WcsRgvblockEntity rgvBlock : rgvBlockList) {
            WcsPlcconfigEntity plcConfig = plcConfigDao.selectByPrimaryKey(rgvBlock.getName());
            String socketStatus = PlcConfigConstant.getStatusString(plcConfig.getStatus());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name", rgvBlock.getName().trim());
            jsonObject.put("McKey", rgvBlock.getMckey());
            jsonObject.put("Appointment_McKey", rgvBlock.getAppointmentMckey());
            jsonObject.put("Command", rgvBlock.getCommand());
            jsonObject.put("Error_Code", rgvBlock.getErrorCode());
            jsonObject.put("Status", rgvBlock.getStatus());
            jsonObject.put("socketStatus", socketStatus);
            jsonObject.put("Is_Load", rgvBlock.getIsLoad());
            jsonObject.put("With_Work_Block_Name", rgvBlock.getWithWorkBlockName());
            jsonObject.put("Berth_Block_Name", rgvBlock.getBerthBlockName());
            jsonObject.put("Reserved1", rgvBlock.getReserved1());
            jsonObject.put("cycleCommand", BlockCache.getString(rgvBlock.getName()) == null ? "" : MessageDetailUtil.getCycleCommandDetail(BlockCache.getString(rgvBlock.getName())));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取母车block列表
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/22 13:57
     */
    @RequestMapping("getMcBlockList")
    @ResponseBody
    public JSONArray getMcBlockList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsMcblockEntity> mcBlockList = DbUtil.getMCBlockDao().selectList(new QueryWrapper<>());
        for (WcsMcblockEntity mcBlock : mcBlockList) {
            WcsPlcconfigEntity plcConfig = plcConfigDao.selectByPrimaryKey(mcBlock.getName());
            String socketStatus = PlcConfigConstant.getStatusString(plcConfig.getStatus());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name", mcBlock.getName().trim());
            jsonObject.put("McKey", mcBlock.getMckey());
            jsonObject.put("Appointment_McKey", mcBlock.getAppointmentMckey());
            jsonObject.put("Command", mcBlock.getCommand());
            jsonObject.put("Error_Code", mcBlock.getErrorCode());
            jsonObject.put("Status", mcBlock.getStatus());
            jsonObject.put("socketStatus", socketStatus);
            jsonObject.put("Is_Load", mcBlock.getIsLoad());
            jsonObject.put("With_Work_Block_Name", mcBlock.getWithWorkBlockName());
            jsonObject.put("Berth_Block_Name", mcBlock.getBerthBlockName());
            jsonObject.put("SC_Block_Name", mcBlock.getScBlockName());
            jsonObject.put("Bing_SC_Block_Name", mcBlock.getBingScBlockName());
            jsonObject.put("Row", mcBlock.getRow());
            jsonObject.put("Line", mcBlock.getLine());
            jsonObject.put("Tier", mcBlock.getTier());
            jsonObject.put("Reserved1", mcBlock.getReserved1());
            jsonObject.put("cycleCommand", BlockCache.getString(mcBlock.getName()) == null ? "" : MessageDetailUtil.getCycleCommandDetail(BlockCache.getString(mcBlock.getName())));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取堆垛机block信息
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/22 13:40
     */
    @RequestMapping("getMlBlockList")
    @ResponseBody
    public JSONArray getMlBlockList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsMlblockEntity> mlBlockList =DbUtil.getMLBlockDao().selectList(new QueryWrapper<>());
        for (WcsMlblockEntity mlBlock : mlBlockList) {
            WcsPlcconfigEntity plcConfig = plcConfigDao.selectByPrimaryKey(mlBlock.getName());
            String socketStatus = PlcConfigConstant.getStatusString(plcConfig.getStatus());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name", mlBlock.getName().trim());
            jsonObject.put("McKey", mlBlock.getMckey());
            jsonObject.put("Appointment_McKey", mlBlock.getAppointmentMckey());
            jsonObject.put("Command", mlBlock.getCommand());
            jsonObject.put("Error_Code", mlBlock.getErrorCode());
            jsonObject.put("Status", mlBlock.getStatus());
            jsonObject.put("socketStatus", socketStatus);
            jsonObject.put("Is_Load", mlBlock.getIsLoad());
            jsonObject.put("With_Work_Block_Name", mlBlock.getWithWorkBlockName());
            jsonObject.put("Berth_Block_Name", mlBlock.getBerthBlockName());
            jsonObject.put("SC_Block_Name", mlBlock.getScBlockName());
            jsonObject.put("Bing_SC_Block_Name", mlBlock.getBingScBlockName());
            jsonObject.put("Is_Move", mlBlock.getIsMove());
            jsonObject.put("Row", mlBlock.getRow());
            jsonObject.put("Line", mlBlock.getLine());
            jsonObject.put("Tier", mlBlock.getTier());
            jsonObject.put("Reserved1", mlBlock.getReserved1());
            jsonObject.put("standbyCar", mlBlock.getIsStandbyCar());
            jsonObject.put("standbyCarBlockName", mlBlock.getStandbyCarBlockName());
            jsonObject.put("cycleCommand", BlockCache.getString(mlBlock.getName()) == null ? "" : MessageDetailUtil.getCycleCommandDetail(BlockCache.getString(mlBlock.getName())));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取输送线block信息
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/22 11:45
     */
    @RequestMapping("getClBlockList")
    @ResponseBody
    public JSONArray getClBlockList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsClblockEntity> clBlockList = DbUtil.getCLBlockDao().selectList(new QueryWrapper<>());
        for (WcsClblockEntity clBlock : clBlockList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name", clBlock.getName().trim());
            jsonObject.put("McKey", clBlock.getMckey());
            jsonObject.put("Appointment_McKey", clBlock.getAppointmentMckey());
            jsonObject.put("Command", clBlock.getCommand());
            jsonObject.put("Error_Code", clBlock.getErrorCode());
            jsonObject.put("Status", clBlock.getStatus());
            jsonObject.put("Is_Load", clBlock.getIsLoad());
            jsonObject.put("With_Work_Block_Name", clBlock.getWithWorkBlockName());
            jsonObject.put("Berth_Block_Name", clBlock.getBerthBlockName());
            jsonObject.put("Reserved1", clBlock.getReserved1());
            jsonObject.put("cycleCommand", BlockCache.getString(clBlock.getName()) == null ? "" : MessageDetailUtil.getCycleCommandDetail(BlockCache.getString(clBlock.getName())));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 获取穿梭车block信息
     *
     * @return com.alibaba.fastjson.JSONArray
     * @author CalmLake
     * @date 2019/2/22 11:45
     */
    @RequestMapping("getScBlockList")
    @ResponseBody
    public JSONArray getScBlockList() {
        JSONArray jsonArray = new JSONArray();
        List<WcsScblockEntity> scBlockList =DbUtil.getSCBlockDao().selectList(new QueryWrapper<>());
        for (WcsScblockEntity scBlock : scBlockList) {
            WcsPlcconfigEntity plcConfig = plcConfigDao.selectByPrimaryKey(scBlock.getName());
            String socketStatus = PlcConfigConstant.getStatusString(plcConfig.getStatus());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name", scBlock.getName().trim());
            jsonObject.put("McKey", scBlock.getMckey());
            jsonObject.put("Appointment_McKey", scBlock.getAppointmentMckey());
            jsonObject.put("Command", scBlock.getCommand());
            jsonObject.put("Error_Code", scBlock.getErrorCode());
            jsonObject.put("Status", scBlock.getStatus());
            jsonObject.put("socketStatus", socketStatus);
            jsonObject.put("Is_Load", scBlock.getIsLoad());
            jsonObject.put("With_Work_Block_Name", scBlock.getWithWorkBlockName());
            jsonObject.put("Berth_Block_Name", scBlock.getBerthBlockName());
            jsonObject.put("Host_Block_Name", scBlock.getHostBlockName());
            jsonObject.put("KWH", scBlock.getKwh());
            jsonObject.put("Last_Work_Time", DateFormatUtil.dateToString(scBlock.getLastWorkTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
            jsonObject.put("Row", scBlock.getRow());
            jsonObject.put("Line", scBlock.getLine());
            jsonObject.put("Tier", scBlock.getTier());
            jsonObject.put("Reserved1", scBlock.getReserved1());
            jsonObject.put("cycleCommand", BlockCache.getString(scBlock.getName()) == null ? "" : MessageDetailUtil.getCycleCommandDetail(BlockCache.getString(scBlock.getName())));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 一键恢复堆垛机和小车为初始状态
     *
     * @return com.alibaba.fastjson.JSONObject
     * @author CalmLake
     * @date 2019/11/18 21:20
     */
    /*@RequestMapping("oneRecovery")
    @ResponseBody
    public JSONObject oneRecovery() {
        JSONObject jsonObject = new JSONObject();
        String msg;
        boolean result;
        if (workPlanDao.getList().size() > 0) {
            msg = "有任务存在";
            result = false;
        } else {
            if ((5 == WcsScblockDaoImpl.getInstance().updateOneRecovery("SC01") +
                    WcsMlblockDaoImpl.getInstance().updateOneRecovery() +
                    WcsClblockDaoImpl.getInstance().updateOneRecovery())) {
                msg = "堆垛机和穿梭车输送线已全部恢复为初始状态！";
                result = true;
            } else {
                msg = "恢复失败！请联系管理猿";
                result = false;
            }
        }
        jsonObject.put("result", result);
        jsonObject.put("msg", msg);
        return jsonObject;
    }*/
}
