package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsAlblockDao;
import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.modules.generator.service.WcsAlblockService;


@Service("wcsAlblockService")
public class WcsAlblockServiceImpl extends ServiceImpl<WcsAlblockDao, WcsAlblockEntity> implements WcsAlblockService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsAlblockEntity> page = this.page(
                new Query<WcsAlblockEntity>().getPage(params),
                new QueryWrapper<WcsAlblockEntity>()
        );

        return new PageUtils(page);
    }

}