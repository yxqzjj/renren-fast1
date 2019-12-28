package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsMcblockEntity;

import java.util.Map;

/**
 * 母车数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
public interface WcsMcblockService extends IService<WcsMcblockEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

