package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsPriorityconfigEntity;

import java.util.Map;

/**
 * 优先级配置表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
public interface WcsPriorityconfigService extends IService<WcsPriorityconfigEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

