package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsDefaultlocationDao;
import io.renren.modules.generator.entity.WcsDefaultlocationEntity;
import io.renren.modules.generator.service.WcsDefaultlocationService;


@Service("wcsDefaultlocationService")
public class WcsDefaultlocationServiceImpl extends ServiceImpl<WcsDefaultlocationDao, WcsDefaultlocationEntity> implements WcsDefaultlocationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsDefaultlocationEntity> page = this.page(
                new Query<WcsDefaultlocationEntity>().getPage(params),
                new QueryWrapper<WcsDefaultlocationEntity>()
        );

        return new PageUtils(page);
    }

}