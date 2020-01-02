package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsRoutestationstartendEntity;
import io.renren.wap.util.DbUtil;

public class WcsRouteStationStartEndDaoImpl {
    private WcsRouteStationStartEndDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsRouteStationStartEndDaoImpl INSTANCE = new WcsRouteStationStartEndDaoImpl();
    }
    public static WcsRouteStationStartEndDaoImpl getRouteStationStartEndDao() {
        return SingletonInstance.INSTANCE;
    }
    public int countNumByFromStationAndEndStation(String startStation,String endStation){
       return  DbUtil.getRouteStationStartEndDao().selectCount(new QueryWrapper<WcsRoutestationstartendEntity>().eq("From_Station",startStation).eq("End_Station",endStation));

    }

}
