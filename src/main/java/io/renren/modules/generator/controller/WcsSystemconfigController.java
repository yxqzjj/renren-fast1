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

import io.renren.modules.generator.entity.WcsSystemconfigEntity;
import io.renren.modules.generator.service.WcsSystemconfigService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * WCS系统配置表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@RestController
@RequestMapping("generator/wcssystemconfig")
public class WcsSystemconfigController {
    @Autowired
    private WcsSystemconfigService wcsSystemconfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcssystemconfig:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsSystemconfigService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcssystemconfig:info")
    public R info(@PathVariable("id") Integer id){
		WcsSystemconfigEntity wcsSystemconfig = wcsSystemconfigService.getById(id);

        return R.ok().put("wcsSystemconfig", wcsSystemconfig);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcssystemconfig:save")
    public R save(@RequestBody WcsSystemconfigEntity wcsSystemconfig){
		wcsSystemconfigService.save(wcsSystemconfig);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcssystemconfig:update")
    public R update(@RequestBody WcsSystemconfigEntity wcsSystemconfig){
		wcsSystemconfigService.updateById(wcsSystemconfig);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcssystemconfig:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsSystemconfigService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
