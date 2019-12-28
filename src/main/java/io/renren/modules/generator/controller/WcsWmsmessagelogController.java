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

import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.modules.generator.service.WcsWmsmessagelogService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * WMS消息记录表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:40:18
 */
@RestController
@RequestMapping("generator/wcswmsmessagelog")
public class WcsWmsmessagelogController {
    @Autowired
    private WcsWmsmessagelogService wcsWmsmessagelogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcswmsmessagelog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsWmsmessagelogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcswmsmessagelog:info")
    public R info(@PathVariable("id") Integer id){
		WcsWmsmessagelogEntity wcsWmsmessagelog = wcsWmsmessagelogService.getById(id);

        return R.ok().put("wcsWmsmessagelog", wcsWmsmessagelog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcswmsmessagelog:save")
    public R save(@RequestBody WcsWmsmessagelogEntity wcsWmsmessagelog){
		wcsWmsmessagelogService.save(wcsWmsmessagelog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcswmsmessagelog:update")
    public R update(@RequestBody WcsWmsmessagelogEntity wcsWmsmessagelog){
		wcsWmsmessagelogService.updateById(wcsWmsmessagelog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcswmsmessagelog:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsWmsmessagelogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
