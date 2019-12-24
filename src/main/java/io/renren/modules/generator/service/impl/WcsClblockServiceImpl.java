package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsClblockDao;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.service.WcsClblockService;


@Service("wcsClblockService")
public class WcsClblockServiceImpl extends ServiceImpl<WcsClblockDao, WcsClblockEntity> implements WcsClblockService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsClblockEntity> page = this.page(
                new Query<WcsClblockEntity>().getPage(params),
                new QueryWrapper<WcsClblockEntity>()
        );

        return new PageUtils(page);
    }

}