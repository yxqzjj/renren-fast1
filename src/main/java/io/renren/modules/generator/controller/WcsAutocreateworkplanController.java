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

import io.renren.modules.generator.entity.WcsAutocreateworkplanEntity;
import io.renren.modules.generator.service.WcsAutocreateworkplanService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 演示模式
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcsautocreateworkplan")
public class WcsAutocreateworkplanController {
    @Autowired
    private WcsAutocreateworkplanService wcsAutocreateworkplanService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsautocreateworkplan:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsAutocreateworkplanService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsautocreateworkplan:info")
    public R info(@PathVariable("id") Integer id){
		WcsAutocreateworkplanEntity wcsAutocreateworkplan = wcsAutocreateworkplanService.getById(id);

        return R.ok().put("wcsAutocreateworkplan", wcsAutocreateworkplan);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsautocreateworkplan:save")
    public R save(@RequestBody WcsAutocreateworkplanEntity wcsAutocreateworkplan){
		wcsAutocreateworkplanService.save(wcsAutocreateworkplan);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsautocreateworkplan:update")
    public R update(@RequestBody WcsAutocreateworkplanEntity wcsAutocreateworkplan){
		wcsAutocreateworkplanService.updateById(wcsAutocreateworkplan);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsautocreateworkplan:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsAutocreateworkplanService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
