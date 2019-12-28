package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsSystemconfigDao;
import io.renren.modules.generator.entity.WcsSystemconfigEntity;
import io.renren.modules.generator.service.WcsSystemconfigService;


@Service("wcsSystemconfigService")
public class WcsSystemconfigServiceImpl extends ServiceImpl<WcsSystemconfigDao, WcsSystemconfigEntity> implements WcsSystemconfigService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsSystemconfigEntity> page = this.page(
                new Query<WcsSystemconfigEntity>().getPage(params),
                new QueryWrapper<WcsSystemconfigEntity>()
        );

        return new PageUtils(page);
    }

}