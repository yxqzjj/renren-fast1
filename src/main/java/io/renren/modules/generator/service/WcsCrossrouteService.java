package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsCrossrouteEntity;

import java.util.Map;

/**
 * 交叉路径信息
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
public interface WcsCrossrouteService extends IService<WcsCrossrouteEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

