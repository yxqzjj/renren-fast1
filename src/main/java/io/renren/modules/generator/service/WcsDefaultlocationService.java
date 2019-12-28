package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsDefaultlocationEntity;

import java.util.Map;

/**
 * 默认位置信息表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
public interface WcsDefaultlocationService extends IService<WcsDefaultlocationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

