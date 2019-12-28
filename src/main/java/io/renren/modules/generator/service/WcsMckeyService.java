package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsMckeyEntity;

import java.util.Map;

/**
 * Mckey生成
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
public interface WcsMckeyService extends IService<WcsMckeyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

