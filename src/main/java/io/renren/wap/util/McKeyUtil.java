package io.renren.wap.util;

import io.renren.modules.generator.entity.WcsMckeyEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * McKey工作类
 *
 * @Author: CalmLake
 * @Date: 2019/1/7  17:12
 * @Version: V1.0.0
 **/
public class McKeyUtil {
    /**
     * mcKey最大值
     */
    private static final int MAX_MC_KEY = 9999;
    /**
     * mcKey最小值
     */
    private static final int MIN_MC_KEY = 1;

    /**
     * 按规则获取一个可用的McKey
     *
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/7 17:21
     */
    public synchronized static String getMcKey() {
        StringBuilder stringBuilder = new StringBuilder();
        String oldMcKeyString = DbUtil.getMcKeyDao().selectById(1).getMckey();
        int oldMcKeyInt = Integer.parseInt(oldMcKeyString);
        int newMcKeyInt = (oldMcKeyInt + 1);
        if (newMcKeyInt > MAX_MC_KEY) {
            newMcKeyInt = MIN_MC_KEY;
            stringBuilder.append(newMcKeyInt);
        } else {
            stringBuilder.append(newMcKeyInt);
        }
        String mcKeyStr = StringUtils.leftPad(stringBuilder.toString(), 4, "0");
        WcsMckeyEntity mcKey = new WcsMckeyEntity();
        mcKey.setMckey(mcKeyStr);
        mcKey.setId(1);
        DbUtil.getMcKeyDao().updateById(mcKey);
        stringBuilder.append(newMcKeyInt);
        return mcKeyStr;
    }
}
