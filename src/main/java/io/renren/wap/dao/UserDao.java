package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsUserEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * 用户
 *
 * @author CalmLake
 * @date 2019/2/19 14:41
 */
@Repository("UserDao")
@Mapper
public interface UserDao {
    @Delete({
            "delete from WCS_User",
            "where Id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into WCS_User (Id, Name, Password)",
            "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=CHAR}, #{password,jdbcType=CHAR})"
    })
    int insert(WcsUserEntity record);

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
    int insertUserNamePassword(@Param("name") String name, @Param("password") String password);



    @Select({
            "select",
            "Id, Name, Password",
            "from WCS_User",
            "where Id = #{id,jdbcType=INTEGER}"
    })
    @Results(id = "user",value = {
            @Result(column = "Id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR),
            @Result(column = "Password", property = "password", jdbcType = JdbcType.CHAR)
    })
    WcsUserEntity selectByPrimaryKey(Integer id);

    /**
     * 此用户名的数量
     *
     * @param name 用户名
     * @return int
     * @author CalmLake
     * @date 2019/2/19 14:59
     */
    @Select({
            "select",
            "count(*)",
            "from WCS_User",
            "where trim(Name)  = #{name,jdbcType=CHAR}"
    })
    int countByName(@Param("name") String name);

    /**
     * 根据用户名查找
     *
     * @param name 用户名
     * @return com.wap.entity.User
     * @author CalmLake
     * @date 2019/2/19 14:39
     */
    @Select({
            "select *  from WCS_User where trim(Name)  = #{name,jdbcType=CHAR} and rownum<=1 "
//            "select  *  from WCS_User   "
    })
    @ResultMap("user")
    WcsUserEntity selectByName(@Param("name") String name);



    @Update({
            "update WCS_User",
            "set Name = #{name,jdbcType=CHAR},",
            "Password = #{password,jdbcType=CHAR}",
            "where Id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WcsUserEntity record);

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
    int updatePasswordByName(@Param("name") String name, @Param("password") String password);
}