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

import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import io.renren.modules.generator.service.WcsWorkplanlogService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 工作计划信息记录
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:10
 */
@RestController
@RequestMapping("generator/wcsworkplanlog")
public class WcsWorkplanlogController {
    @Autowired
    private WcsWorkplanlogService wcsWorkplanlogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsworkplanlog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsWorkplanlogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsworkplanlog:info")
    public R info(@PathVariable("id") Integer id){
		WcsWorkplanlogEntity wcsWorkplanlog = wcsWorkplanlogService.getById(id);

        return R.ok().put("wcsWorkplanlog", wcsWorkplanlog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsworkplanlog:save")
    public R save(@RequestBody WcsWorkplanlogEntity wcsWorkplanlog){
		wcsWorkplanlogService.save(wcsWorkplanlog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsworkplanlog:update")
    public R update(@RequestBody WcsWorkplanlogEntity wcsWorkplanlog){
		wcsWorkplanlogService.updateById(wcsWorkplanlog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsworkplanlog:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsWorkplanlogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
