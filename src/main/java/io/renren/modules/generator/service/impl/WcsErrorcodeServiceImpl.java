package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsErrorcodeDao;
import io.renren.modules.generator.entity.WcsErrorcodeEntity;
import io.renren.modules.generator.service.WcsErrorcodeService;


@Service("wcsErrorcodeService")
public class WcsErrorcodeServiceImpl extends ServiceImpl<WcsErrorcodeDao, WcsErrorcodeEntity> implements WcsErrorcodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsErrorcodeEntity> page = this.page(
                new Query<WcsErrorcodeEntity>().getPage(params),
                new QueryWrapper<WcsErrorcodeEntity>()
        );

        return new PageUtils(page);
    }

}