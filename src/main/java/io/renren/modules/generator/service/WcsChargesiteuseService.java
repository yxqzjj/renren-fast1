package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsChargesiteuseEntity;

import java.util.Map;

/**
 * 充电位使用状态表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
public interface WcsChargesiteuseService extends IService<WcsChargesiteuseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

