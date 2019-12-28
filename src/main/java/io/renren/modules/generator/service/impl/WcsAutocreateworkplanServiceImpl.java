package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsAutocreateworkplanDao;
import io.renren.modules.generator.entity.WcsAutocreateworkplanEntity;
import io.renren.modules.generator.service.WcsAutocreateworkplanService;


@Service("wcsAutocreateworkplanService")
public class WcsAutocreateworkplanServiceImpl extends ServiceImpl<WcsAutocreateworkplanDao, WcsAutocreateworkplanEntity> implements WcsAutocreateworkplanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsAutocreateworkplanEntity> page = this.page(
                new Query<WcsAutocreateworkplanEntity>().getPage(params),
                new QueryWrapper<WcsAutocreateworkplanEntity>()
        );

        return new PageUtils(page);
    }

}