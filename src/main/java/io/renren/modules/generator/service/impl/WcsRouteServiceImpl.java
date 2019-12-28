package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsRouteDao;
import io.renren.modules.generator.entity.WcsRouteEntity;
import io.renren.modules.generator.service.WcsRouteService;


@Service("wcsRouteService")
public class WcsRouteServiceImpl extends ServiceImpl<WcsRouteDao, WcsRouteEntity> implements WcsRouteService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsRouteEntity> page = this.page(
                new Query<WcsRouteEntity>().getPage(params),
                new QueryWrapper<WcsRouteEntity>()
        );

        return new PageUtils(page);
    }

}