package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsTaskingDao;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.modules.generator.service.WcsTaskingService;


@Service("wcsTaskingService")
public class WcsTaskingServiceImpl extends ServiceImpl<WcsTaskingDao, WcsTaskingEntity> implements WcsTaskingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsTaskingEntity> page = this.page(
                new Query<WcsTaskingEntity>().getPage(params),
                new QueryWrapper<WcsTaskingEntity>()
        );

        return new PageUtils(page);
    }

}