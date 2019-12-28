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

import io.renren.modules.generator.entity.WcsRgvblockEntity;
import io.renren.modules.generator.service.WcsRgvblockService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * RGV数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcsrgvblock")
public class WcsRgvblockController {
    @Autowired
    private WcsRgvblockService wcsRgvblockService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsrgvblock:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsRgvblockService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsrgvblock:info")
    public R info(@PathVariable("id") Integer id){
		WcsRgvblockEntity wcsRgvblock = wcsRgvblockService.getById(id);

        return R.ok().put("wcsRgvblock", wcsRgvblock);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsrgvblock:save")
    public R save(@RequestBody WcsRgvblockEntity wcsRgvblock){
		wcsRgvblockService.save(wcsRgvblock);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsrgvblock:update")
    public R update(@RequestBody WcsRgvblockEntity wcsRgvblock){
		wcsRgvblockService.updateById(wcsRgvblock);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsrgvblock:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsRgvblockService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
