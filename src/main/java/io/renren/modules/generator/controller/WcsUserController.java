package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.WcsUserEntity;
import io.renren.modules.generator.service.WcsUserService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 用户表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@RestController
@RequestMapping("generator/wcsuser")
public class WcsUserController {
    @Autowired
    private WcsUserService wcsUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsuser:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsUserService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsuser:info")
    public R info(@PathVariable("id") Integer id){
		WcsUserEntity wcsUser = wcsUserService.getById(id);

        return R.ok().put("wcsUser", wcsUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsuser:save")
    public R save(@RequestBody WcsUserEntity wcsUser){
		wcsUserService.save(wcsUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsuser:update")
    public R update(@RequestBody WcsUserEntity wcsUser){
		wcsUserService.updateById(wcsUser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsuser:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
