package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.WcsChargesiteuseDao;
import io.renren.modules.generator.entity.WcsChargeEntity;
import io.renren.wap.util.DbUtil;

public class WcsChargeDaoImpl{
    private WcsChargeDaoImpl() {}
    private static class SingletonInstance {
        private static final WcsChargeDaoImpl INSTANCE = new WcsChargeDaoImpl();
    }
    public  static WcsChargeDaoImpl getChargeDao() {
        return WcsChargeDaoImpl.SingletonInstance.INSTANCE;
    }
    public  WcsChargeEntity selectByPrimaryKey(String blockName){
        return DbUtil.getChargeDao().selectOne(new QueryWrapper<WcsChargeEntity>().eq("Block_Name",blockName));
    }
    public int deleteByPrimaryKey(String blockName){
       return DbUtil.getChargeDao().delete(new QueryWrapper<WcsChargeEntity>().eq("Block_Name",blockName));
    }
}
