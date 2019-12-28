package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsScblockDao;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.service.WcsScblockService;


@Service("wcsScblockService")
public class WcsScblockServiceImpl extends ServiceImpl<WcsScblockDao, WcsScblockEntity> implements WcsScblockService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsScblockEntity> page = this.page(
                new Query<WcsScblockEntity>().getPage(params),
                new QueryWrapper<WcsScblockEntity>()
        );

        return new PageUtils(page);
    }

}