package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsUserDao;
import io.renren.modules.generator.entity.WcsUserEntity;
import io.renren.modules.generator.service.WcsUserService;


@Service("wcsUserService")
public class WcsUserServiceImpl extends ServiceImpl<WcsUserDao, WcsUserEntity> implements WcsUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsUserEntity> page = this.page(
                new Query<WcsUserEntity>().getPage(params),
                new QueryWrapper<WcsUserEntity>()
        );

        return new PageUtils(page);
    }

}