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

import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.service.WcsClblockService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 输送线数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcsclblock")
public class WcsClblockController {
    @Autowired
    private WcsClblockService wcsClblockService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsclblock:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsClblockService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsclblock:info")
    public R info(@PathVariable("id") Integer id){
		WcsClblockEntity wcsClblock = wcsClblockService.getById(id);

        return R.ok().put("wcsClblock", wcsClblock);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsclblock:save")
    public R save(@RequestBody WcsClblockEntity wcsClblock){
		wcsClblockService.save(wcsClblock);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsclblock:update")
    public R update(@RequestBody WcsClblockEntity wcsClblock){
		wcsClblockService.updateById(wcsClblock);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsclblock:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsClblockService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
