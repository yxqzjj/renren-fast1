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

import io.renren.modules.generator.entity.WcsDefaultlocationEntity;
import io.renren.modules.generator.service.WcsDefaultlocationService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 默认位置信息表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcsdefaultlocation")
public class WcsDefaultlocationController {
    @Autowired
    private WcsDefaultlocationService wcsDefaultlocationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsdefaultlocation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsDefaultlocationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsdefaultlocation:info")
    public R info(@PathVariable("id") Integer id){
		WcsDefaultlocationEntity wcsDefaultlocation = wcsDefaultlocationService.getById(id);

        return R.ok().put("wcsDefaultlocation", wcsDefaultlocation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsdefaultlocation:save")
    public R save(@RequestBody WcsDefaultlocationEntity wcsDefaultlocation){
		wcsDefaultlocationService.save(wcsDefaultlocation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsdefaultlocation:update")
    public R update(@RequestBody WcsDefaultlocationEntity wcsDefaultlocation){
		wcsDefaultlocationService.updateById(wcsDefaultlocation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsdefaultlocation:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsDefaultlocationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
