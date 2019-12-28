package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsAutocreateworkplanEntity;

import java.util.Map;

/**
 * 演示模式
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
public interface WcsAutocreateworkplanService extends IService<WcsAutocreateworkplanEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

