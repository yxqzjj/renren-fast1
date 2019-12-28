package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WcsWarehouseDao;
import io.renren.modules.generator.entity.WcsWarehouseEntity;
import io.renren.modules.generator.service.WcsWarehouseService;


@Service("wcsWarehouseService")
public class WcsWarehouseServiceImpl extends ServiceImpl<WcsWarehouseDao, WcsWarehouseEntity> implements WcsWarehouseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WcsWarehouseEntity> page = this.page(
                new Query<WcsWarehouseEntity>().getPage(params),
                new QueryWrapper<WcsWarehouseEntity>()
        );

        return new PageUtils(page);
    }

}