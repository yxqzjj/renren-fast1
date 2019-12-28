package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsChargeDao;
import io.renren.modules.generator.entity.WcsChargeEntity;
import io.renren.modules.generator.service.WcsChargeService;


@Service("wcsChargeService")
public class WcsChargeServiceImpl extends ServiceImpl<WcsChargeDao, WcsChargeEntity> implements WcsChargeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsChargeEntity> page = this.page(
                new Query<WcsChargeEntity>().getPage(params),
                new QueryWrapper<WcsChargeEntity>()
        );

        return new PageUtils(page);
    }

}