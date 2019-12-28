package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsBlocknameEntity;

import java.util.Map;

/**
 * 不可分配任务数据Block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
public interface WcsBlocknameService extends IService<WcsBlocknameEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

