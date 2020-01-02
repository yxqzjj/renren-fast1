package io.renren.wap.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsRouteEntity;
import io.renren.wap.util.DbUtil;

/**
 * 路径
 *
 * @Author: CalmLake
 * @Date: 2019/3/8  16:50
 * @Version: V1.0.0
 **/
public class RouteService {

    /**
     * 获取路径中下一设备的block名称
     *
     * @param blockName 数据block名称
     * @param toStation 目标地名
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/3/8 16:51
     */
    public static String getRouteNextBlockName(String blockName, String toStation) {
        WcsRouteEntity route = DbUtil.getRouteDao().selectOne(new QueryWrapper<WcsRouteEntity>()
                .eq("blockName",blockName).eq("To_Station",toStation));
        return route.getNextBlockName();
    }
}
