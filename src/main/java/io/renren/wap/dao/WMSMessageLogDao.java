package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.wap.dao.provider.WMSMessageLogProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * wms消息记录
 *
 * @author CalmLake
 * @date 2019/1/8 10:29
 */
@Repository("WMSMessageLogDao")
@Mapper
public interface WMSMessageLogDao {
    @Delete({
            "delete from WCS_WMSMessageLog",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Delete({
            "delete from WCS_WMSMessageLog"
    })
    int deleteAll();

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


    /**
     * 根据wmsId查找
     *
     * @param wmsId wms任务唯一标识
     * @return com.wap.entity.WmsMessageLog
     * @author CalmLake
     * @date 2019/1/8 13:09
     */
    @Select({
            "select",
            "ID, WMS_ID, Work_Plan_ID, Type, Create_Time, Barcode, Status, Message, Reserved1, ",
            "Reserved2",
            "from WCS_WMSMessageLog",
            "where trim(WMS_ID) = #{wmsId,jdbcType=VARCHAR}"
    })
    @Results(id = "wmsMessageLog", value = {
            @Result(column = "ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "WMS_ID", property = "wmsId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Work_Plan_ID", property = "workPlanId", jdbcType = JdbcType.INTEGER),
            @Result(column = "Type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Create_Time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Barcode", property = "barcode", jdbcType = JdbcType.CHAR),
            @Result(column = "Status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "Message", property = "message", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UUID", property = "uuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR)
    })
    WcsWmsmessagelogEntity selectByWmsId(@Param("wmsId") String wmsId);

    /**
     * 功能描述
     *
     * @param barCode 托盘码
     * @return com.wap.entity.WmsMessageLog
     * @author CalmLake
     * @date 2019/8/20 13:43
     */
    @Select({
            "select",
            "   *",
            "from WCS_WMSMessageLog",
            "where trim(Barcode) = #{barcode,jdbcType=CHAR} and Type= 'LoadUnitAtId' and  rownum <= 1 order by id desc"
    })
    @ResultMap("wmsMessageLog")
    WcsWmsmessagelogEntity selectByBarCode(@Param("barcode") String barCode);

    /**
     * 获取所有信息
     *
     * @return WmsMessageLog
     * @author CalmLake
     * @date 2019/4/1 15:06
     */
    @Select({
            "select * from WCS_WMSMessageLog order by id desc"
    })
    @ResultMap("wmsMessageLog")
    List<WcsWmsmessagelogEntity> getList();

    /**
     * 根据主键查找
     *
     * @param id 主键
     * @return WmsMessageLog
     * @author CalmLake
     * @date 2019/4/1 15:06
     */
    @Select({
            "select * from WCS_WMSMessageLog",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("wmsMessageLog")
    WcsWmsmessagelogEntity selectByPrimaryKey(Integer id);

    /**
     * 根据uuid查找已发送的LoadUnitAtId信息
     *
     * @param uuid 唯一标识信息
     * @return com.wap.entity.WmsMessageLog
     * @author CalmLake
     * @date 2019/5/18 15:04
     */
    @Select({
            "select * from WCS_WMSMessageLog",
            "where trim(UUID) = #{uuid,jdbcType=VARCHAR}  and Type='LoadUnitAtId'"
    })
    @ResultMap("wmsMessageLog")
    WcsWmsmessagelogEntity selectByUuId(@Param("uuid") String uuid);

    /**
     * 修改消息状态
     *
     * @param uuid   消息唯一标识
     * @param status 状态
     * @return int
     * @author CalmLake
     * @date 2019/5/18 15:12
     */
    @Update({
            "update WCS_WMSMessageLog set Status=#{status,jdbcType =TINYINT} where trim(UUID) = #{uuid,jdbcType=VARCHAR} "
    })
    int updateStatusByUuid(@Param("uuid") String uuid, @Param("status") Integer status);

    /**
     * 根据序号修改消息状态
     *
     * @param id     序号
     * @param status 状态
     * @return int
     * @author CalmLake
     * @date 2019/8/20 13:49
     */
    @Update({
            "update WCS_WMSMessageLog set Status=#{status,jdbcType =TINYINT} where ID = #{id,jdbcType=INTEGER} "
    })
    int updateStatusById(@Param("id") Integer id, @Param("status") Integer status);
}