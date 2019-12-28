package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.generator.entity.GoodsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-17 13:28:39
 */
@Mapper
public interface GoodsDao extends BaseMapper<GoodsEntity> {

}
