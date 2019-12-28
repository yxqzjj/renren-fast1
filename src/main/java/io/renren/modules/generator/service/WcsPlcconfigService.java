package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;

import java.util.Map;

/**
 * plc配置表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
public interface WcsPlcconfigService extends IService<WcsPlcconfigEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

