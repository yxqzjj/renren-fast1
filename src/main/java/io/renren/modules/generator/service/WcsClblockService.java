package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsClblockEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-20 10:25:28
 */
public interface WcsClblockService extends IService<WcsClblockEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

