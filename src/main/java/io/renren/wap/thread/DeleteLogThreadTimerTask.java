package io.renren.wap.thread;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.constant.DatePatternConstant;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DeleteFileUtil;

import java.util.Date;
import java.util.TimerTask;

/**
 * 删除日志
 *
 * @Author: CalmLake
 * @Date: 2019/4/24  10:53
 * @Version: V1.0.0
 **/
public class DeleteLogThreadTimerTask extends TimerTask {
    @Override
    public void run() {
        String path = SystemCache.LOGS_PATH;
        int reserveTime = SystemCache.LOG_RESERVE_TIME;
        long nowDateLong = System.currentTimeMillis();
        long dayLong = 1000 * 60 * 60 * 24;
        long oldLong = (nowDateLong - dayLong * reserveTime);
        Date oldDate = new Date(oldLong);
        String fileName = DateFormatUtil.dateToString(oldDate, DatePatternConstant.YYYY_MM_DD);
        String file = path + fileName;
        DeleteFileUtil.getInstance().delete(file);
    }
}
