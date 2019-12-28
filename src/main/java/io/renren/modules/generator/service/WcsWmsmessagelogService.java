package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;

import java.util.Map;

/**
 * WMS消息记录表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:40:18
 */
public interface WcsWmsmessagelogService extends IService<WcsWmsmessagelogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

