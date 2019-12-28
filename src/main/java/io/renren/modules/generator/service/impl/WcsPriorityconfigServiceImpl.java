package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsPriorityconfigDao;
import io.renren.modules.generator.entity.WcsPriorityconfigEntity;
import io.renren.modules.generator.service.WcsPriorityconfigService;


@Service("wcsPriorityconfigService")
public class WcsPriorityconfigServiceImpl extends ServiceImpl<WcsPriorityconfigDao, WcsPriorityconfigEntity> implements WcsPriorityconfigService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsPriorityconfigEntity> page = this.page(
                new Query<WcsPriorityconfigEntity>().getPage(params),
                new QueryWrapper<WcsPriorityconfigEntity>()
        );

        return new PageUtils(page);
    }

}