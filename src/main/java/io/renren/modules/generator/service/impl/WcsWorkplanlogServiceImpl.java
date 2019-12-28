package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsWorkplanlogDao;
import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import io.renren.modules.generator.service.WcsWorkplanlogService;


@Service("wcsWorkplanlogService")
public class WcsWorkplanlogServiceImpl extends ServiceImpl<WcsWorkplanlogDao, WcsWorkplanlogEntity> implements WcsWorkplanlogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsWorkplanlogEntity> page = this.page(
                new Query<WcsWorkplanlogEntity>().getPage(params),
                new QueryWrapper<WcsWorkplanlogEntity>()
        );

        return new PageUtils(page);
    }

}