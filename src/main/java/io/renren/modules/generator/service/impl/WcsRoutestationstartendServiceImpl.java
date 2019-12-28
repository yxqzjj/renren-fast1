package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsRoutestationstartendDao;
import io.renren.modules.generator.entity.WcsRoutestationstartendEntity;
import io.renren.modules.generator.service.WcsRoutestationstartendService;


@Service("wcsRoutestationstartendService")
public class WcsRoutestationstartendServiceImpl extends ServiceImpl<WcsRoutestationstartendDao, WcsRoutestationstartendEntity> implements WcsRoutestationstartendService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsRoutestationstartendEntity> page = this.page(
                new Query<WcsRoutestationstartendEntity>().getPage(params),
                new QueryWrapper<WcsRoutestationstartendEntity>()
        );

        return new PageUtils(page);
    }

}