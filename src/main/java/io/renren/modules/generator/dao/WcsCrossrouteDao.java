package io.renren.modules.generator.dao;

import io.renren.modules.generator.dao.provider.CrossRouteSqlProvider;
import io.renren.modules.generator.entity.WcsCrossrouteEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * 交叉路径信息
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Mapper
public interface WcsCrossrouteDao extends BaseMapper<WcsCrossrouteEntity> {
    /**
     * 修改交叉路径信息
     *
     * @param crossRoute Run_Block_Name-必须有值
     * @return int
     * @author CalmLake
     * @date 2019/3/11 16:05
     */
    @UpdateProvider(type = CrossRouteSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(WcsCrossrouteEntity crossRoute);

}
