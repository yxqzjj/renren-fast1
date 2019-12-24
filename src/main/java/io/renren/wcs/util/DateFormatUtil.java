package io.renren.wcs.util;

import io.renren.wcs.constant.DatePatternConstant;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * 时间工具类
 *
 * @Author: CalmLake
 * @Date: 2019/1/8  11:08
 * @Version: V1.0.0
 **/
public class DateFormatUtil {
    /**
     * 获取时分秒字符串 格式HHmmss
     *
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/15 9:57
     */
    public static String getStringHHmmss() {
        return dateToString(new Date(), DatePatternConstant.HH_MM_SS);
    }

    /**
     * date 转 string  格式 yyyy-MM-dd HH:mm:ssZZ
     *
     * @param date 时间
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/8 11:10
     */
    public static String dateToString(Date date) {
        return DateFormatUtils.format(date, DatePatternConstant.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * date 转 string  自定义格式
     *
     * @param date-时间, pattern-格式
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/8 14:01
     */
    public static String dateToString(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }
}
