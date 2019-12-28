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

import io.renren.modules.generator.entity.WcsChargeEntity;
import io.renren.modules.generator.service.WcsChargeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 充电信息表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcscharge")
public class WcsChargeController {
    @Autowired
    private WcsChargeService wcsChargeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcscharge:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsChargeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcscharge:info")
    public R info(@PathVariable("id") Integer id){
		WcsChargeEntity wcsCharge = wcsChargeService.getById(id);

        return R.ok().put("wcsCharge", wcsCharge);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcscharge:save")
    public R save(@RequestBody WcsChargeEntity wcsCharge){
		wcsChargeService.save(wcsCharge);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcscharge:update")
    public R update(@RequestBody WcsChargeEntity wcsCharge){
		wcsChargeService.updateById(wcsCharge);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcscharge:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsChargeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
