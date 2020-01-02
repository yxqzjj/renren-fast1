package io.renren.wap.thread;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.constant.DatePatternConstant;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DeleteFileUtil;
import io.renren.wap.util.Log4j2Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时删除日志
 *
 * @Author: CalmLake
 * @date 2019/8/11  11:06
 * @Version: V1.0.0
 **/
@Component
public class DeleteLogScheduled {

    @Scheduled(cron = "0 0 2 * * ?")
    public void regularTimeExport() {
        String path = SystemCache.LOGS_PATH;
        int reserveTime = SystemCache.LOG_RESERVE_TIME;
        long nowDateLong = System.currentTimeMillis();
        long dayLong = 1000 * 60 * 60 * 24;
        long oldLong = (nowDateLong - dayLong * reserveTime);
        Date oldDate = new Date(oldLong);
        String fileName = DateFormatUtil.dateToString(oldDate, DatePatternConstant.YYYY_MM_DD);
        String file = path + fileName;
        DeleteFileUtil.getInstance().delete(file);
        Log4j2Util.getRoot().info(String.format("删除log日志执行完毕，%s", file));
    }

}
