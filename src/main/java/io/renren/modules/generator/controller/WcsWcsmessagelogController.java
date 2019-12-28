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

import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;
import io.renren.modules.generator.service.WcsWcsmessagelogService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * WCS消息记录表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@RestController
@RequestMapping("generator/wcswcsmessagelog")
public class WcsWcsmessagelogController {
    @Autowired
    private WcsWcsmessagelogService wcsWcsmessagelogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcswcsmessagelog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsWcsmessagelogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcswcsmessagelog:info")
    public R info(@PathVariable("id") Integer id){
		WcsWcsmessagelogEntity wcsWcsmessagelog = wcsWcsmessagelogService.getById(id);

        return R.ok().put("wcsWcsmessagelog", wcsWcsmessagelog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcswcsmessagelog:save")
    public R save(@RequestBody WcsWcsmessagelogEntity wcsWcsmessagelog){
		wcsWcsmessagelogService.save(wcsWcsmessagelog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcswcsmessagelog:update")
    public R update(@RequestBody WcsWcsmessagelogEntity wcsWcsmessagelog){
		wcsWcsmessagelogService.updateById(wcsWcsmessagelog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcswcsmessagelog:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsWcsmessagelogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
