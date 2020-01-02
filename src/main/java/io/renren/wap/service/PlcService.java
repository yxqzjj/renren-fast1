package io.renren.wap.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wap.util.DbUtil;

import java.util.Date;

/**
 * plc
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  14:51
 * @Version: V1.0.0
 **/
public class PlcService {
    /**
     * 修改心跳时间
     *
     * @param plcName plc名称
     * @author CalmLake
     * @date 2019/1/16 14:56
     */
    public void updateHeartTime(String plcName) {
        WcsPlcconfigEntity plcconfigEntity=DbUtil.getPlcConfigDao().selectOne(new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
        plcconfigEntity.setHeartbeatTime(new Date());
        DbUtil.getPlcConfigDao().update(plcconfigEntity,new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
    }
}
