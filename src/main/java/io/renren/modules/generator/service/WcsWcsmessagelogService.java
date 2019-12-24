package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-20 11:06:48
 */
public interface WcsWcsmessagelogService extends IService<WcsWcsmessagelogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

