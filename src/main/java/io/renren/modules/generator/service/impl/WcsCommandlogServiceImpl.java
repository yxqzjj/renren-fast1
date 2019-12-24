package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsCommandlogDao;
import io.renren.modules.generator.entity.WcsCommandlogEntity;
import io.renren.modules.generator.service.WcsCommandlogService;


@Service("wcsCommandlogService")
public class WcsCommandlogServiceImpl extends ServiceImpl<WcsCommandlogDao, WcsCommandlogEntity> implements WcsCommandlogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsCommandlogEntity> page = this.page(
                new Query<WcsCommandlogEntity>().getPage(params),
                new QueryWrapper<WcsCommandlogEntity>()
        );

        return new PageUtils(page);
    }

}