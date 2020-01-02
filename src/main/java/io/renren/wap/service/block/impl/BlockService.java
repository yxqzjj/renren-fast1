package io.renren.wap.service.block.impl;


import io.renren.modules.generator.dao.impl.*;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.StorageAreaService;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据block服务类
 *
 * @Author: CalmLake
 * @Date: 2019/1/9  12:05
 * @Version: V1.0.0
 **/
public class BlockService {

    /**
     * 穿梭车状态是否可以工作
     *
     * @param status       穿梭车状态
     * @param workPlanType 工作计划类型
     * @return boolean
     * @author CalmLake
     * @date 2019/3/20 14:23
     */
    boolean isScStatusCanWork(String status, Integer workPlanType) {
        if (BlockConstant.STATUS_CHARGE.equals(status)) {
            return !(WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType || WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType
                    || WorkPlanConstant.TYPE_MOVEMENT==workPlanType || WorkPlanConstant.TYPE_TALLY==workPlanType
                    || WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType || WorkPlanConstant.TYPE_TAKE_STOCK==workPlanType
                    || WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType);
        } else {
            return true;
        }
    }

    /**
     * 设备是否可以发送消息
     *
     * @param object 设备信息
     * @return boolean
     * @author CalmLake
     * @date 2019/3/6 10:47
     */
    int getKeyType(Object object) {
        String mcKey = null;
        String appointmentMcKey = null;
        if (object instanceof WcsScblockEntity) {
            mcKey = ((WcsScblockEntity) object).getMckey();
            appointmentMcKey = ((WcsScblockEntity) object).getAppointmentMckey();
        } else if (object instanceof WcsAlblockEntity) {
            mcKey = ((WcsAlblockEntity) object).getMckey();
            appointmentMcKey = ((WcsAlblockEntity) object).getAppointmentMckey();
        } else if (object instanceof WcsMcblockEntity) {
            mcKey = ((WcsMcblockEntity) object).getMckey();
            appointmentMcKey = ((WcsMcblockEntity) object).getAppointmentMckey();
        } else if (object instanceof WcsMlblockEntity) {
            mcKey = ((WcsMlblockEntity) object).getMckey();
            appointmentMcKey = ((WcsMlblockEntity) object).getAppointmentMckey();
        } else if (object instanceof WcsRgvblockEntity) {
            mcKey = ((WcsRgvblockEntity) object).getMckey();
            appointmentMcKey = ((WcsRgvblockEntity) object).getAppointmentMckey();
        } else if (object instanceof WcsClblockEntity) {
            mcKey = ((WcsClblockEntity) object).getMckey();
            appointmentMcKey = ((WcsClblockEntity) object).getAppointmentMckey();
        } else {
            Log4j2Util.getBlockBrickLogger().info("判断设备是否可以发送消息时类型转换出错");
        }
        if (StringUtils.isEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
            return BlockConstant.KEY_EMPTY;
        } else if (StringUtils.isNotEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
            return BlockConstant.MCKEY_NOT_EMPTY;
        } else if (StringUtils.isEmpty(mcKey) && StringUtils.isNotEmpty(appointmentMcKey)) {
            return BlockConstant.APPOINTMENT_MCKEY_NOT_EMPTY;
        } else {
            return BlockConstant.KEY_NOT_EMPTY;
        }
    }

    /**
     * 设备是否可以发送消息
     *
     * @param block 设备信息
     * @return boolean
     * @author CalmLake
     * @date 2019/3/6 10:47
     */
    boolean isCanSendMsg(Block block) {
        String blockName = block.getName();
        String command = block.getCommand();
        String errorCode = block.getErrorCode();
        String mcKey = block.getMckey();
        String appointmentMcKey = block.getAppointmentMckey();
        Log4j2Util.getBlockBrickLogger().info(String.format("消息发送判断条件：blockName：%s，command：%s，errorCode：%s，mcKey：%s，appointmentMcKey：%s", blockName, command, errorCode, mcKey, appointmentMcKey));
        if (!BlockConstant.ERROR_CODE_DEFAULT.equals(errorCode)) {
            return false;
        }
        if (StringUtils.isEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
            return false;
        }
        return MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK.equals(command) || MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT.equals(command);
    }

    /**
     * 修改当前异常状态
     *
     * @param object    操作表的对象
     * @param blockName 数据block名称
     * @param errorCode 异常码
     * @return int
     * @author CalmLake
     * @date 2019/1/16 17:00
     */
    public int updateBlockErrorCode(String errorCode, Object object, String blockName) {
        if (object instanceof WcsMlblockDaoImpl) {
            return ((WcsMlblockDaoImpl) object).updateBlockErrorCodeByPrimaryKey(blockName, errorCode);
        } else if (object instanceof WcsScblockDaoImpl) {
            return ((WcsScblockDaoImpl) object).updateBlockErrorCodeByPrimaryKey(blockName, errorCode);
        } else if (object instanceof WcsMcblockDaoImpl) {
            return ((WcsMcblockDaoImpl) object).updateBlockErrorCodeByPrimaryKey(blockName, errorCode);
        } else if (object instanceof WcsRgvblockDaoImpl) {
            return ((WcsRgvblockDaoImpl) object).updateBlockErrorCodeByPrimaryKey(blockName, errorCode);
        } else if (object instanceof WcsAlblockDaoImpl) {
            return ((WcsAlblockDaoImpl) object).updateBlockErrorCodeByPrimaryKey(blockName, errorCode);
        } else if (object instanceof WcsClblockDaoImpl) {
            return ((WcsClblockDaoImpl) object).updateBlockErrorCodeByPrimaryKey(blockName, errorCode);
        } else {
            return -1;
        }
    }

    /**
     * 修改DB command
     *
     * @param command   命令类型
     * @param object    dao操作对象
     * @param blockName 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/16 9:57
     */
    public int updateCommand(String command, Object object, String blockName) {
        return updateBlockCommand(command, object, blockName);
    }

    /**
     * 修改block表中命令类型和载荷状态
     *
     * @param loadStatus 载荷状态
     * @param command    命令类型
     * @param object     dao操作对象
     * @param blockName  数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/3/1 16:56
     */
    public static int updateBlockCommandAndLoadStatus(String command, boolean loadStatus, Object object, String blockName) {
        if (object instanceof WcsMlblockDaoImpl) {
            return ((WcsMlblockDaoImpl) object).updateCommandAndLoad(blockName, command, loadStatus);
        } else if (object instanceof WcsScblockDaoImpl) {
            return ((WcsScblockDaoImpl) object).updateCommandAndLoad(blockName, command, loadStatus);
        } else if (object instanceof WcsMcblockDaoImpl) {
            return ((WcsMcblockDaoImpl) object).updateCommandAndLoad(blockName, command, loadStatus);
        } else if (object instanceof WcsRgvblockDaoImpl) {
            return ((WcsRgvblockDaoImpl) object).updateCommandAndLoad(blockName, command, loadStatus);
        } else if (object instanceof WcsAlblockDaoImpl) {
            return ((WcsAlblockDaoImpl) object).updateCommandAndLoad(blockName, command, loadStatus);
        } else if (object instanceof WcsClblockDaoImpl) {
            return ((WcsClblockDaoImpl) object).updateCommandAndLoad(blockName, command, loadStatus);
        } else {
            return -1;
        }
    }

    /**
     * 修改block表中 command
     *
     * @param command   命令类型
     * @param object    dao操作对象
     * @param blockName 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/1/16 9:46
     */
    public int updateBlockCommand(String command, Object object, String blockName) {
        if (object instanceof WcsMlblockDaoImpl) {
            return ((WcsMlblockDaoImpl) object).updateCommandByPrimaryKey(blockName, command);
        } else if (object instanceof WcsScblockDaoImpl) {
            return ((WcsScblockDaoImpl) object).updateCommandByPrimaryKey(blockName, command);
        } else if (object instanceof WcsMcblockDaoImpl) {
            return ((WcsMcblockDaoImpl) object).updateCommandByPrimaryKey(blockName, command);
        } else if (object instanceof WcsRgvblockDaoImpl) {
            return ((WcsRgvblockDaoImpl) object).updateCommandByPrimaryKey(blockName, command);
        } else if (object instanceof WcsAlblockDaoImpl) {
            return ((WcsAlblockDaoImpl) object).updateCommandByPrimaryKey(blockName, command);
        } else if (object instanceof WcsClblockDaoImpl) {
            return ((WcsClblockDaoImpl) object).updateCommandByPrimaryKey(blockName, command);
        } else {
            return -1;
        }
    }

    /**
     * 判断两个设备的位置是否一致  穿梭车和堆垛机或穿梭车和母车
     *
     * @param row               排
     * @param line              列
     * @param tier,             层
     * @param withWorkBlockName 交互设备数据block名称
     * @return boolean
     * @author CalmLake
     * @date 2019/1/15 14:35
     */
    boolean isSameSiteOfTwoMachine(String row, String line, String tier, String withWorkBlockName) {
        String row2 = "0";
        String line2 = "0";
        String tier2 = "0";
        if (withWorkBlockName.contains(MachineConstant.TYPE_MC)) {
            WcsMcblockEntity mcBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
            row2 = mcBlock.getRow();
            line2 = mcBlock.getLine();
            tier2 = mcBlock.getTier();
        } else if (withWorkBlockName.contains(MachineConstant.TYPE_ML)) {
            WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
            row2 = mlBlock.getRow();
            line2 = mlBlock.getLine();
            tier2 = mlBlock.getTier();
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("对比两台设备位置时没有找到对应的解析类型,withWorkBlockName:%s", withWorkBlockName));
        }
        return isSameSite(row, line, tier, row2, line2, tier2);
    }

    /**
     * 比较两个货位是否处于同一巷道
     *
     * @param locationA 货位A
     * @param locationB 货位B
     * @return boolean
     * @author CalmLake
     * @date 2019/7/30 15:55
     */
    public boolean isSameArea(String locationA, String locationB) {
        String rowString = locationA.substring(0, 3);
        String lineString = locationA.substring(3, 6);
        String tierString = locationA.substring(6, 9);
        return isSameArea(rowString, lineString, tierString, locationB);
    }

    /**
     * 是否处于同一区域（同一段不中断的巷道）
     *
     * @param row      排
     * @param line     列
     * @param tier,    层
     * @param location 货位 001002003 排列层每个长度为3
     * @return boolean
     * @author CalmLake
     * @date 2019/1/10 14:27
     */
    boolean isSameArea(String row, String line, String tier, String location) {
        int rowInt = Integer.parseInt(row);
        int lineInt = Integer.parseInt(line);
        int tierInt = Integer.parseInt(tier);
        int rowInt2 = Integer.parseInt(location.substring(0, 3));
        int lineInt2 = Integer.parseInt(location.substring(3, 6));
        int tierInt2 = Integer.parseInt(location.substring(6, 9));
        if (tierInt == tierInt2) {
            if (lineInt == lineInt2 && rowInt == rowInt2) {
                return true;
            } else {
                switch (SystemCache.SYS_NAME_COMPANY) {
                    case CompanyConstant.SYS_NAME_COMPANY_KERISOM:
                        if (rowInt == rowInt2) {
                            return StorageAreaService.isSameArea_JIALISEN(lineInt, lineInt2);
                        } else {
                            return false;
                        }
                    case CompanyConstant.SYS_NAME_COMPANY_YOU_JI:
                        if (lineInt == lineInt2) {
                            return StorageAreaService.isSameArea_YOUJI(rowInt, rowInt2);
                        } else {
                            return false;
                        }
                    case CompanyConstant.SYS_NAME_COMPANY_YONG_XIANG:
                        if (lineInt == lineInt2) {
                            return StorageAreaService.isSameArea_YONGXIANG(rowInt, rowInt2);
                        } else {
                            return false;
                        }
                    case CompanyConstant.SYS_NAME_COMPANY_YAN_SHI_QU:
                        if (lineInt == lineInt2) {
                            return StorageAreaService.isSameArea_YANSHIQU(rowInt, rowInt2);
                        } else {
                            return false;
                        }
                    case CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN:
                        if (lineInt == lineInt2) {
                            return StorageAreaService.isSameArea_JIATIAN(rowInt, rowInt2);
                        } else {
                            return false;
                        }
                    case CompanyConstant.SYS_NAME_COMPANY_MU_JV_CANG:
                        if (lineInt == lineInt2) {
                            return StorageAreaService.isSameArea_MUJVCANG(rowInt, rowInt2);
                        } else {
                            return false;
                        }
                    default:
                        return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 位置相同
     *
     * @param row   排
     * @param line  列
     * @param tier, 层
     * @param row2  排2
     * @param line2 列2
     * @param tier2 层2
     * @return boolean
     * @author CalmLake
     * @date 2019/1/10 13:56
     */
    boolean isSameSite(String row, String line, String tier, String row2, String line2, String tier2) {
        int rowInt = Integer.parseInt(row);
        int lineInt = Integer.parseInt(line);
        int tierInt = Integer.parseInt(tier);
        int rowInt2 = Integer.parseInt(row2);
        int lineInt2 = Integer.parseInt(line2);
        int tierInt2 = Integer.parseInt(tier2);
        return (rowInt == rowInt2 && lineInt == lineInt2 && tierInt == tierInt2);
    }

    /**
     * 位置相同
     *
     * @param tier,    层
     * @param row      排
     * @param line     列
     * @param location 货位 001002003 排列层每个长度为3
     * @return boolean
     * @author CalmLake
     * @date 2019/1/9 17:20
     */
    public boolean isSameSite(String row, String line, String tier, String location) {
        int rowInt = Integer.parseInt(row);
        int lineInt = Integer.parseInt(line);
        int tierInt = Integer.parseInt(tier);
        int rowInt2 = Integer.parseInt(location.substring(0, 3));
        int lineInt2 = Integer.parseInt(location.substring(3, 6));
        int tierInt2 = Integer.parseInt(location.substring(6, 9));
        return (rowInt == rowInt2 && lineInt == lineInt2 && tierInt == tierInt2);
    }

    /**
     * 已经完成工作
     *
     * @param command 当前已执行命令
     * @return boolean
     * @author CalmLake
     * @date 2019/1/9 13:30
     */
    public static boolean isFinishWork(String command) {
        return MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT.equals(command) || MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK.equals(command);
    }

    /**
     * 不是异常
     *
     * @param errorCode 错误代码
     * @return boolean
     * @author CalmLake
     * @date 2019/1/9 13:25
     */
    public static boolean isNotError(String errorCode) {
        return BlockConstant.ERROR_CODE_DEFAULT.equals(errorCode);
    }
}
