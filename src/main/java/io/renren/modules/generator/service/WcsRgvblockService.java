package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsRgvblockEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-20 09:26:00
 */
public interface WcsRgvblockService extends IService<WcsRgvblockEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

