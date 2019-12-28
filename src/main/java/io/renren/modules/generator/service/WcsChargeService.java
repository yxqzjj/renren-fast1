package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsChargeEntity;

import java.util.Map;

/**
 * 充电信息表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
public interface WcsChargeService extends IService<WcsChargeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

