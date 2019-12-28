package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;

import java.util.Map;

/**
 * WCS消息记录表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
public interface WcsWcsmessagelogService extends IService<WcsWcsmessagelogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

