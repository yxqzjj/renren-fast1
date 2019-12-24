package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsPlcconfigDao;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.modules.generator.service.WcsPlcconfigService;


@Service("wcsPlcconfigService")
public class WcsPlcconfigServiceImpl extends ServiceImpl<WcsPlcconfigDao, WcsPlcconfigEntity> implements WcsPlcconfigService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsPlcconfigEntity> page = this.page(
                new Query<WcsPlcconfigEntity>().getPage(params),
                new QueryWrapper<WcsPlcconfigEntity>()
        );

        return new PageUtils(page);
    }

}