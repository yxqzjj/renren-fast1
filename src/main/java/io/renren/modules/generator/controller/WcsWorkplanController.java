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

import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.modules.generator.service.WcsWorkplanService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 工作计划表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:10
 */
@RestController
@RequestMapping("generator/wcsworkplan")
public class WcsWorkplanController {
    @Autowired
    private WcsWorkplanService wcsWorkplanService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsworkplan:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsWorkplanService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsworkplan:info")
    public R info(@PathVariable("id") Integer id){
		WcsWorkplanEntity wcsWorkplan = wcsWorkplanService.getById(id);

        return R.ok().put("wcsWorkplan", wcsWorkplan);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsworkplan:save")
    public R save(@RequestBody WcsWorkplanEntity wcsWorkplan){
		wcsWorkplanService.save(wcsWorkplan);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsworkplan:update")
    public R update(@RequestBody WcsWorkplanEntity wcsWorkplan){
		wcsWorkplanService.updateById(wcsWorkplan);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsworkplan:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsWorkplanService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
