package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsStationmodeDao;
import io.renren.modules.generator.entity.WcsStationmodeEntity;
import io.renren.modules.generator.service.WcsStationmodeService;


@Service("wcsStationmodeService")
public class WcsStationmodeServiceImpl extends ServiceImpl<WcsStationmodeDao, WcsStationmodeEntity> implements WcsStationmodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsStationmodeEntity> page = this.page(
                new Query<WcsStationmodeEntity>().getPage(params),
                new QueryWrapper<WcsStationmodeEntity>()
        );

        return new PageUtils(page);
    }

}