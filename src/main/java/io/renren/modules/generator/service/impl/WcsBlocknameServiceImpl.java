package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsBlocknameDao;
import io.renren.modules.generator.entity.WcsBlocknameEntity;
import io.renren.modules.generator.service.WcsBlocknameService;


@Service("wcsBlocknameService")
public class WcsBlocknameServiceImpl extends ServiceImpl<WcsBlocknameDao, WcsBlocknameEntity> implements WcsBlocknameService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsBlocknameEntity> page = this.page(
                new Query<WcsBlocknameEntity>().getPage(params),
                new QueryWrapper<WcsBlocknameEntity>()
        );

        return new PageUtils(page);
    }

}