package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsMachineDao;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.service.WcsMachineService;


@Service("wcsMachineService")
public class WcsMachineServiceImpl extends ServiceImpl<WcsMachineDao, WcsMachineEntity> implements WcsMachineService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsMachineEntity> page = this.page(
                new Query<WcsMachineEntity>().getPage(params),
                new QueryWrapper<WcsMachineEntity>()
        );

        return new PageUtils(page);
    }

}