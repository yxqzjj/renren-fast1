package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsCrossrouteDao;
import io.renren.modules.generator.entity.WcsCrossrouteEntity;
import io.renren.modules.generator.service.WcsCrossrouteService;


@Service("wcsCrossrouteService")
public class WcsCrossrouteServiceImpl extends ServiceImpl<WcsCrossrouteDao, WcsCrossrouteEntity> implements WcsCrossrouteService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsCrossrouteEntity> page = this.page(
                new Query<WcsCrossrouteEntity>().getPage(params),
                new QueryWrapper<WcsCrossrouteEntity>()
        );

        return new PageUtils(page);
    }

}