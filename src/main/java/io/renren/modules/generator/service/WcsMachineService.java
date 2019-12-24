package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WcsMachineEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-20 16:39:45
 */
public interface WcsMachineService extends IService<WcsMachineEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

