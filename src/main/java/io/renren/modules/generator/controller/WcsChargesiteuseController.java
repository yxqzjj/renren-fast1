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

import io.renren.modules.generator.entity.WcsChargesiteuseEntity;
import io.renren.modules.generator.service.WcsChargesiteuseService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 充电位使用状态表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcschargesiteuse")
public class WcsChargesiteuseController {
    @Autowired
    private WcsChargesiteuseService wcsChargesiteuseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcschargesiteuse:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsChargesiteuseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcschargesiteuse:info")
    public R info(@PathVariable("id") Integer id){
		WcsChargesiteuseEntity wcsChargesiteuse = wcsChargesiteuseService.getById(id);

        return R.ok().put("wcsChargesiteuse", wcsChargesiteuse);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcschargesiteuse:save")
    public R save(@RequestBody WcsChargesiteuseEntity wcsChargesiteuse){
		wcsChargesiteuseService.save(wcsChargesiteuse);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcschargesiteuse:update")
    public R update(@RequestBody WcsChargesiteuseEntity wcsChargesiteuse){
		wcsChargesiteuseService.updateById(wcsChargesiteuse);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcschargesiteuse:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsChargesiteuseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
