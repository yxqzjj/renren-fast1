package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsMlblockDao;
import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.service.WcsMlblockService;


@Service("wcsMlblockService")
public class WcsMlblockServiceImpl extends ServiceImpl<WcsMlblockDao, WcsMlblockEntity> implements WcsMlblockService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsMlblockEntity> page = this.page(
                new Query<WcsMlblockEntity>().getPage(params),
                new QueryWrapper<WcsMlblockEntity>()
        );

        return new PageUtils(page);
    }

}