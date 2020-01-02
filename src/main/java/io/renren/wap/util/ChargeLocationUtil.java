package io.renren.wap.util;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsChargesiteuseEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.wap.cache.SystemCache;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 充电位信息操作
 *
 * @author CalmLake
 * @date 2019/4/17 18:01
 */
public class ChargeLocationUtil {
    private static ChargeLocationUtil ourInstance = new ChargeLocationUtil();

    public static ChargeLocationUtil getInstance() {
        return ourInstance;
    }

    private ChargeLocationUtil() {
    }

    /**
     * 充电位置分配
     *
     * @param blockName 数据block
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/4/17 18:07
     */
    public synchronized String assigningLocation(String blockName) {
        String location = "";
        try {
            List<WcsChargesiteuseEntity> chargeSiteUseList = DbUtil.getChargeSiteUseDao().selectList(new QueryWrapper<WcsChargesiteuseEntity>());
            for (WcsChargesiteuseEntity chargeSiteUse : chargeSiteUseList) {
                if (StringUtils.isEmpty(chargeSiteUse.getBlockName())) {
                    location = chargeSiteUse.getLocation();
                    chargeSiteUse.setBlockName(blockName);
                    DbUtil.getChargeSiteUseDao().updateById(chargeSiteUse);
                    return location;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            location = "";
        }
        return location;
    }

    /**
     * 充电位置回收
     *
     * @param blockName 数据block
     * @author CalmLake
     * @date 2019/4/17 18:07
     */
    public synchronized void recycleLocation(String blockName) {
        try {
            List<WcsChargesiteuseEntity> chargeSiteUseList = DbUtil.getChargeSiteUseDao().selectList(new QueryWrapper<WcsChargesiteuseEntity>());
            for (WcsChargesiteuseEntity chargeSiteUse : chargeSiteUseList) {
                if (StringUtils.isNotEmpty(chargeSiteUse.getBlockName()) && chargeSiteUse.getBlockName().equals(blockName)) {
                    chargeSiteUse.setBlockName("");
                    DbUtil.getChargeSiteUseDao().updateById(chargeSiteUse);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取穿梭车（备车）位置  目前备车固定放置一特殊位置
     * @author CalmLake
     * @date 2019/4/19 17:24
     * @param blockName 穿梭车名称
     * @return java.lang.String
     */
    public String getLocation(String blockName) {
        String location = null;
        WcsChargesiteuseEntity chargeSiteUse = DbUtil.getChargeSiteUseDao().selectOne(new QueryWrapper<WcsChargesiteuseEntity>().eq("Block_Name",blockName));
        if (chargeSiteUse == null) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
            if (scBlock.getIsStandbyCar()) {
                location = SystemCache.STANDBY_CAR_LOCATION;
            }
        } else {
            location = chargeSiteUse.getLocation();
        }
        return location;
    }
}
