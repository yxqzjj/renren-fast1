package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsWorkplanEntity;

import java.util.Map;

/**
 * 工作计划表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:10
 */
public interface WcsWorkplanService extends IService<WcsWorkplanEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

