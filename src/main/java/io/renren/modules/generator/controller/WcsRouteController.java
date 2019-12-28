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

import io.renren.modules.generator.entity.WcsRouteEntity;
import io.renren.modules.generator.service.WcsRouteService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 路径信息表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcsroute")
public class WcsRouteController {
    @Autowired
    private WcsRouteService wcsRouteService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsroute:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsRouteService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsroute:info")
    public R info(@PathVariable("id") Integer id){
		WcsRouteEntity wcsRoute = wcsRouteService.getById(id);

        return R.ok().put("wcsRoute", wcsRoute);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsroute:save")
    public R save(@RequestBody WcsRouteEntity wcsRoute){
		wcsRouteService.save(wcsRoute);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsroute:update")
    public R update(@RequestBody WcsRouteEntity wcsRoute){
		wcsRouteService.updateById(wcsRoute);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsroute:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsRouteService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
