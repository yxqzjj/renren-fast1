package io.renren.wap.dao.provider;


import io.renren.modules.generator.entity.WcsAlblockEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/3/5  17:08
 * @Version: V1.0.0
 **/
public class ALBlockProvider {

    /**
     * 根据主键修改升降机对象数据
     */
    public String updateALBlockProvider(WcsAlblockEntity alBlock) {
        SQL sql = new SQL();
        sql.UPDATE("WCS_ALBlock");
        if (StringUtils.isNotEmpty(alBlock.getReserved2())) {
            sql.SET("Reserved2 = #{reserved2,jdbcType=VARCHAR}");
        }
        if (StringUtils.isNotEmpty(alBlock.getReserved1())) {
            sql.SET("Reserved1 = #{reserved1,jdbcType=VARCHAR}");
        }
        if (StringUtils.isNotEmpty(alBlock.getTier())) {
            sql.SET("Tier = #{tier,jdbcType=CHAR}");
        }
        if (StringUtils.isNotEmpty(alBlock.getBerthBlockName())) {
            sql.SET("Berth_Block_Name = #{berthBlockName,jdbcType=CHAR}");
        }
        if (StringUtils.isNotEmpty(alBlock.getAppointmentMckey())) {
            sql.SET("Appointment_McKey = #{appointmentMcKey,jdbcType=CHAR}");
        }
        if (StringUtils.isNotEmpty(alBlock.getMckey())) {
            sql.SET("McKey = #{mcKey,jdbcType=CHAR}");
        }
        if (StringUtils.isNotEmpty(alBlock.getCommand())) {
            sql.SET("Command = #{command,jdbcType=NCHAR}");
        }
        if (StringUtils.isNotEmpty(alBlock.getErrorCode())) {
            sql.SET("Error_Code = #{errorCode,jdbcType=NCHAR}");
        }
        if (StringUtils.isNotEmpty(alBlock.getStatus())) {
            sql.SET("Status = #{status,jdbcType=NCHAR}");
        }
        if (alBlock.getIsLoad() != null) {
            sql.SET("Is_Load = #{isLoad,jdbcType=BIT}");
        }
        if (StringUtils.isNotEmpty(alBlock.getWithWorkBlockName())) {
            sql.SET("With_Work_Block_Name = #{withWorkBlockName,jdbcType=CHAR}");
        }
        sql.WHERE("Name = #{name,jdbcType=CHAR}");
        return sql.toString();
    }
}
