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

import io.renren.modules.generator.entity.WcsStationmodeEntity;
import io.renren.modules.generator.service.WcsStationmodeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 站台模式表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@RestController
@RequestMapping("generator/wcsstationmode")
public class WcsStationmodeController {
    @Autowired
    private WcsStationmodeService wcsStationmodeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsstationmode:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsStationmodeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsstationmode:info")
    public R info(@PathVariable("id") Integer id){
		WcsStationmodeEntity wcsStationmode = wcsStationmodeService.getById(id);

        return R.ok().put("wcsStationmode", wcsStationmode);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsstationmode:save")
    public R save(@RequestBody WcsStationmodeEntity wcsStationmode){
		wcsStationmodeService.save(wcsStationmode);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsstationmode:update")
    public R update(@RequestBody WcsStationmodeEntity wcsStationmode){
		wcsStationmodeService.updateById(wcsStationmode);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsstationmode:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsStationmodeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
