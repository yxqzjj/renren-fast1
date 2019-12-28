package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsClcrossroutedetailEntity;

import java.util.Map;

/**
 * 输送线交叉路径详情
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
public interface WcsClcrossroutedetailService extends IService<WcsClcrossroutedetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

