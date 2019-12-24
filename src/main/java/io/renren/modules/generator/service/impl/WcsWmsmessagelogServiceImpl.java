package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsWmsmessagelogDao;
import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.modules.generator.service.WcsWmsmessagelogService;


@Service("wcsWmsmessagelogService")
public class WcsWmsmessagelogServiceImpl extends ServiceImpl<WcsWmsmessagelogDao, WcsWmsmessagelogEntity> implements WcsWmsmessagelogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsWmsmessagelogEntity> page = this.page(
                new Query<WcsWmsmessagelogEntity>().getPage(params),
                new QueryWrapper<WcsWmsmessagelogEntity>()
        );

        return new PageUtils(page);
    }

}