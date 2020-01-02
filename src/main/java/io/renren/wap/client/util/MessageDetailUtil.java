package io.renren.wap.client.util;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.entity.constant.CompanyConstant;

/**
 * 命令转化工具类
 *
 * @Author: CalmLake
 * @Date: 2019/3/19  10:18
 * @Version: V1.0.0
 **/
public class MessageDetailUtil {

    /**
     * 获取cycle指令对应的文字详情
     *
     * @param cycleCommand cycle指令
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/2/21 17:22
     */
    public static String getCycleCommandDetail(String cycleCommand) {
        switch (cycleCommand) {
            case "01":
                return "返回原点";
            case "02":
                return "取货";
            case "03":
                return "卸货";
            case "04":
                return "移动";
            case "05":
                return "接子车";
            case "06":
                return "卸子车";
            case "07":
                return "移载取货";
            case "08":
                return "移载卸货";
            case "09":
                return "空车上车";
            case "10":
                return "空车下车";
            case "11":
                if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
                    return "充电开始";
                } else {
                    return "载货移动";
                }
            case "12":
                if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
                    return "充电完成";
                } else {
                    return "载货上车";
                }
            case "13":
                return "载货下车";
            case "14":
                return "充电开始";
            case "15":
                return "充电完成";
            case "16":
                return "盘点";
            case "17":
                return "理货";
            case "18":
                return "下车至输送线接驳台A 段";
            case "19":
                return "下车至输送线接驳台B 段";
            case "20":
                return "输送线接驳台上提升机";
            case "21":
                return "输送线接驳台上母车";
            default:
                return "";
        }
    }

    /**
     * 获取载荷状态对应的文字详情
     *
     * @param load 载荷状态
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/2/21 17:32
     */
    public static String getLoadDetail(String load) {
        switch (load) {
            case "0":
                return "无";
            case "1":
                return "托盘";
            case "2":
                return "子车";
            case "3":
                return "子车托盘";
            default:
                return "";
        }
    }

    /**
     * 获取作业区分对应的文字详情
     *
     * @param cycleType 作业区分
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/2/21 17:28
     */
    public static String getCycleTypeDetail(String cycleType) {
        switch (cycleType) {
            case "01":
                return "入库";
            case "02":
                return "直行";
            case "03":
                return "整出库";
            case "04":
                return "拣选出库";
            case "05":
                return "补充出库";
            case "06":
                return "回库";
            case "07":
                return "充电开始";
            case "08":
                if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
                    return "充电完成";
                } else {
                    return "换层";
                }
            case "09":
                return "充电完成";
            case "10":
                return "盘点";
            case "11":
            case "13":
                return "移库";
            case "12":
                return "理货";
            default:
                return "";
        }
    }
}
