package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsChargesiteuseDao;
import io.renren.modules.generator.entity.WcsChargesiteuseEntity;
import io.renren.modules.generator.service.WcsChargesiteuseService;


@Service("wcsChargesiteuseService")
public class WcsChargesiteuseServiceImpl extends ServiceImpl<WcsChargesiteuseDao, WcsChargesiteuseEntity> implements WcsChargesiteuseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsChargesiteuseEntity> page = this.page(
                new Query<WcsChargesiteuseEntity>().getPage(params),
                new QueryWrapper<WcsChargesiteuseEntity>()
        );

        return new PageUtils(page);
    }

}