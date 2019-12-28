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

import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.modules.generator.service.WcsTaskingService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 任务分配表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@RestController
@RequestMapping("generator/wcstasking")
public class WcsTaskingController {
    @Autowired
    private WcsTaskingService wcsTaskingService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcstasking:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsTaskingService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcstasking:info")
    public R info(@PathVariable("id") Integer id){
		WcsTaskingEntity wcsTasking = wcsTaskingService.getById(id);

        return R.ok().put("wcsTasking", wcsTasking);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcstasking:save")
    public R save(@RequestBody WcsTaskingEntity wcsTasking){
		wcsTaskingService.save(wcsTasking);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcstasking:update")
    public R update(@RequestBody WcsTaskingEntity wcsTasking){
		wcsTaskingService.updateById(wcsTasking);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcstasking:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsTaskingService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
