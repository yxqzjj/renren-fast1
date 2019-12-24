package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsRgvblockDao;
import io.renren.modules.generator.entity.WcsRgvblockEntity;
import io.renren.modules.generator.service.WcsRgvblockService;


@Service("wcsRgvblockService")
public class WcsRgvblockServiceImpl extends ServiceImpl<WcsRgvblockDao, WcsRgvblockEntity> implements WcsRgvblockService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsRgvblockEntity> page = this.page(
                new Query<WcsRgvblockEntity>().getPage(params),
                new QueryWrapper<WcsRgvblockEntity>()
        );

        return new PageUtils(page);
    }

}