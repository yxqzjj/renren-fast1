package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsWorkplanDao;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.modules.generator.service.WcsWorkplanService;


@Service("wcsWorkplanService")
public class WcsWorkplanServiceImpl extends ServiceImpl<WcsWorkplanDao, WcsWorkplanEntity> implements WcsWorkplanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsWorkplanEntity> page = this.page(
                new Query<WcsWorkplanEntity>().getPage(params),
                new QueryWrapper<WcsWorkplanEntity>()
        );

        return new PageUtils(page);
    }

}