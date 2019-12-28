package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsWarehouseEntity;

import java.util.Map;

/**
 * 仓库库区表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
public interface WcsWarehouseService extends IService<WcsWarehouseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

