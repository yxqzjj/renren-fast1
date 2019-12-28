package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsRgvblockEntity;

import java.util.Map;

/**
 * RGV数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
public interface WcsRgvblockService extends IService<WcsRgvblockEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

