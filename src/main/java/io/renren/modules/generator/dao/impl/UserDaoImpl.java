package io.renren.modules.generator.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsUserEntity;
import io.renren.wap.util.DbUtil;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public class UserDaoImpl {
    private UserDaoImpl() {}
    private static class SingletonInstance {
        private static final UserDaoImpl INSTANCE = new UserDaoImpl();
    }
    public static UserDaoImpl getUserDao() {
        return UserDaoImpl.SingletonInstance.INSTANCE;
    }
    /**
     * 此用户名的数量
     *
     * @param name 用户名
     * @return int
     * @author CalmLake
     * @date 2019/2/19 14:59
     */
    public int countByName(String name){
     return DbUtil.getWcsUserDao().selectCount(new QueryWrapper<WcsUserEntity>().eq("Name",name));
    }
    /**
     * 新增一条用户记录
     *
     * @param name     用户名
     * @param password 密码
     * @return int
     * @author CalmLake
     * @date 2019/2/19 14:39
     */
    @Insert({
            "insert into WCS_User ( Name, Password)",
            "values ( #{name,jdbcType=CHAR}, #{password,jdbcType=CHAR})"
    })
   public int insertUserNamePassword(String name, String password){
        WcsUserEntity wcsUserEntity=new WcsUserEntity();
        wcsUserEntity.setName(name);
        wcsUserEntity.setPassword(password);
        return DbUtil.getWcsUserDao().insert(wcsUserEntity);
    }

    /**
     * 更改密码
     *
     * @param name     用户
     * @param password 密码
     * @return int
     * @author CalmLake
     * @date 2019/2/19 14:38
     */
    @Update({
            "update WCS_User",
            "set ",
            "Password = #{password,jdbcType=CHAR}",
            "where trim(Name) = #{name,jdbcType=INTEGER}"
    })
   public int updatePasswordByName( String name,String password){
        WcsUserEntity wcsUserEntity=  DbUtil.getWcsUserDao().selectOne(new QueryWrapper<WcsUserEntity>().eq("Name",name));
        wcsUserEntity.setPassword(password);
        return DbUtil.getWcsUserDao().updateById(wcsUserEntity);
    }

}
