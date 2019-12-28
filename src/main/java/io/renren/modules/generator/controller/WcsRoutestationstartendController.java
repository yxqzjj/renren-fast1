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

import io.renren.modules.generator.entity.WcsRoutestationstartendEntity;
import io.renren.modules.generator.service.WcsRoutestationstartendService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 路径起止表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcsroutestationstartend")
public class WcsRoutestationstartendController {
    @Autowired
    private WcsRoutestationstartendService wcsRoutestationstartendService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsroutestationstartend:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsRoutestationstartendService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsroutestationstartend:info")
    public R info(@PathVariable("id") Integer id){
		WcsRoutestationstartendEntity wcsRoutestationstartend = wcsRoutestationstartendService.getById(id);

        return R.ok().put("wcsRoutestationstartend", wcsRoutestationstartend);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsroutestationstartend:save")
    public R save(@RequestBody WcsRoutestationstartendEntity wcsRoutestationstartend){
		wcsRoutestationstartendService.save(wcsRoutestationstartend);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsroutestationstartend:update")
    public R update(@RequestBody WcsRoutestationstartendEntity wcsRoutestationstartend){
		wcsRoutestationstartendService.updateById(wcsRoutestationstartend);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsroutestationstartend:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsRoutestationstartendService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
