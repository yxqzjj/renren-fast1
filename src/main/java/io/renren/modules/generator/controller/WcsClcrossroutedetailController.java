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

import io.renren.modules.generator.entity.WcsClcrossroutedetailEntity;
import io.renren.modules.generator.service.WcsClcrossroutedetailService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 输送线交叉路径详情
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcsclcrossroutedetail")
public class WcsClcrossroutedetailController {
    @Autowired
    private WcsClcrossroutedetailService wcsClcrossroutedetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsclcrossroutedetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsClcrossroutedetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsclcrossroutedetail:info")
    public R info(@PathVariable("id") Integer id){
		WcsClcrossroutedetailEntity wcsClcrossroutedetail = wcsClcrossroutedetailService.getById(id);

        return R.ok().put("wcsClcrossroutedetail", wcsClcrossroutedetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsclcrossroutedetail:save")
    public R save(@RequestBody WcsClcrossroutedetailEntity wcsClcrossroutedetail){
		wcsClcrossroutedetailService.save(wcsClcrossroutedetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsclcrossroutedetail:update")
    public R update(@RequestBody WcsClcrossroutedetailEntity wcsClcrossroutedetail){
		wcsClcrossroutedetailService.updateById(wcsClcrossroutedetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsclcrossroutedetail:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsClcrossroutedetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
