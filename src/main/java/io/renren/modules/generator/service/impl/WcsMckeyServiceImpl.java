package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsMckeyDao;
import io.renren.modules.generator.entity.WcsMckeyEntity;
import io.renren.modules.generator.service.WcsMckeyService;


@Service("wcsMckeyService")
public class WcsMckeyServiceImpl extends ServiceImpl<WcsMckeyDao, WcsMckeyEntity> implements WcsMckeyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsMckeyEntity> page = this.page(
                new Query<WcsMckeyEntity>().getPage(params),
                new QueryWrapper<WcsMckeyEntity>()
        );

        return new PageUtils(page);
    }

}