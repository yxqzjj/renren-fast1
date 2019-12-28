package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsAlblockEntity;

import java.util.Map;

/**
 * 升降机数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
public interface WcsAlblockService extends IService<WcsAlblockEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

