package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsStationmodeEntity;

import java.util.Map;

/**
 * 站台模式表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
public interface WcsStationmodeService extends IService<WcsStationmodeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

