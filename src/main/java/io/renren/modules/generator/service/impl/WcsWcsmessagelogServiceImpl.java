package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsWcsmessagelogDao;
import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;
import io.renren.modules.generator.service.WcsWcsmessagelogService;


@Service("wcsWcsmessagelogService")
public class WcsWcsmessagelogServiceImpl extends ServiceImpl<WcsWcsmessagelogDao, WcsWcsmessagelogEntity> implements WcsWcsmessagelogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsWcsmessagelogEntity> page = this.page(
                new Query<WcsWcsmessagelogEntity>().getPage(params),
                new QueryWrapper<WcsWcsmessagelogEntity>()
        );

        return new PageUtils(page);
    }

}