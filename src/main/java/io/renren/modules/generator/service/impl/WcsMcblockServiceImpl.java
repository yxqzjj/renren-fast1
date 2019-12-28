package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsMcblockDao;
import io.renren.modules.generator.entity.WcsMcblockEntity;
import io.renren.modules.generator.service.WcsMcblockService;


@Service("wcsMcblockService")
public class WcsMcblockServiceImpl extends ServiceImpl<WcsMcblockDao, WcsMcblockEntity> implements WcsMcblockService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsMcblockEntity> page = this.page(
                new Query<WcsMcblockEntity>().getPage(params),
                new QueryWrapper<WcsMcblockEntity>()
        );

        return new PageUtils(page);
    }

}