package io.renren.wap.thread;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;
import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时删除信息记录表数据
 *
 * @Author: CalmLake
 * @date 2019/8/11  11:06
 * @Version: V1.0.0
 **/
@Component
public class DeleteTableLogScheduled {

    @Scheduled(cron = "0 0 3 * * ?")
    public void regularTimeExport() {
        int result1 = DbUtil.getWCSMessageLogDao().delete(new QueryWrapper<WcsWcsmessagelogEntity>());
        int result2 = DbUtil.getWMSMessageLogDao().delete(new QueryWrapper<WcsWmsmessagelogEntity>());
        int result3 = DbUtil.getCommandLogDao().delete(new QueryWrapper<>());
        Log4j2Util.getRoot().info(String.format("删除信息记录表数据执行完毕，%d ,%d ,%d ,", result1, result2, result3));
    }

}
