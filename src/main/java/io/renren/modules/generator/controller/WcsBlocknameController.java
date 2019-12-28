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

import io.renren.modules.generator.entity.WcsBlocknameEntity;
import io.renren.modules.generator.service.WcsBlocknameService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 不可分配任务数据Block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcsblockname")
public class WcsBlocknameController {
    @Autowired
    private WcsBlocknameService wcsBlocknameService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsblockname:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsBlocknameService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsblockname:info")
    public R info(@PathVariable("id") Integer id){
		WcsBlocknameEntity wcsBlockname = wcsBlocknameService.getById(id);

        return R.ok().put("wcsBlockname", wcsBlockname);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsblockname:save")
    public R save(@RequestBody WcsBlocknameEntity wcsBlockname){
		wcsBlocknameService.save(wcsBlockname);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsblockname:update")
    public R update(@RequestBody WcsBlocknameEntity wcsBlockname){
		wcsBlocknameService.updateById(wcsBlockname);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsblockname:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsBlocknameService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
