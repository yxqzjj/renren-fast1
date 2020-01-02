package io.renren.wap.service.msg;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/1/15  15:27
 * @Version: V1.0.0
 **/
class MsgService {
    /**
     * 获取排
     *
     * @param location 001002003 排列层每个长度为3
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/15 16:01
     */
    String getRow(String location) {
        return location.substring(0, 3).substring(1, 3);
    }

    /**
     * 获取列
     *
     * @param location 001002003 排列层每个长度为3
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/15 16:01
     */
    String getLine(String location) {
        return location.substring(3, 6).substring(1, 3);
    }

    /**
     * 获取层
     *
     * @param location 001002003 排列层每个长度为3
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/15 16:01
     */
    String getTier(String location) {
        return location.substring(6, 9).substring(1, 3);
    }

    /**
     * 类型转换
     *
     * @param workPlanType 工作计划类型
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/15 12:16
     */
    String getCycleType(Integer workPlanType) {
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_PUT_IN_STORAGE_01;
        } else if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_OUTPUT_ALL_STORAGE_03;
        } else if (WorkPlanConstant.TYPE_MOVEMENT==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)){
                return "13";
            }else {
                return MsgCycleOrderConstant.CYCLE_TYPE_MOVEMENT_11;
            }
        } else if (WorkPlanConstant.TYPE_TALLY==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_TALLY_15;
        } else if (WorkPlanConstant.TYPE_TAKE_STOCK==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_TAKE_STOCK_12;
        } else if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_CHARGE_UP_07;
        } else if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_CHARGE_COMPLETE_09;
        } else if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_CHANGE_TIER_08;
        }else if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_OUTPUT_ALL_STORAGE_03;
        }else if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
            return MsgCycleOrderConstant.CYCLE_TYPE_OUTPUT_ALL_STORAGE_03;
        }  else {
            return MsgCycleOrderConstant.CYCLE_TYPE_GO_STRAIGHT_02;
        }
    }
}
