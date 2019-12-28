package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsClcrossroutedetailDao;
import io.renren.modules.generator.entity.WcsClcrossroutedetailEntity;
import io.renren.modules.generator.service.WcsClcrossroutedetailService;


@Service("wcsClcrossroutedetailService")
public class WcsClcrossroutedetailServiceImpl extends ServiceImpl<WcsClcrossroutedetailDao, WcsClcrossroutedetailEntity> implements WcsClcrossroutedetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsClcrossroutedetailEntity> page = this.page(
                new Query<WcsClcrossroutedetailEntity>().getPage(params),
                new QueryWrapper<WcsClcrossroutedetailEntity>()
        );

        return new PageUtils(page);
    }

}