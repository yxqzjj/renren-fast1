package io.renren.modules.generator.dao;

import io.renren.modules.generator.dao.provider.WMSMessageLogProvider;
import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * WMS消息记录表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:40:18
 */
@Mapper
public interface WcsWmsmessagelogDao extends BaseMapper<WcsWmsmessagelogEntity> {
    /**
     * 动态sql插入 且返回主键id至对象
     *
     * @param wmsMessageLog 数据
     * @return int
     * @author CalmLake
     * @date 2019/1/8 13:07
     */
    @InsertProvider(type = WMSMessageLogProvider.class, method = "insertProvider")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int insertProvider(WcsWmsmessagelogEntity wmsMessageLog);

}
