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

import io.renren.modules.generator.entity.WcsCommandlogEntity;
import io.renren.modules.generator.service.WcsCommandlogService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 命令消息记录表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcscommandlog")
public class WcsCommandlogController {
    @Autowired
    private WcsCommandlogService wcsCommandlogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcscommandlog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsCommandlogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcscommandlog:info")
    public R info(@PathVariable("id") Integer id){
		WcsCommandlogEntity wcsCommandlog = wcsCommandlogService.getById(id);

        return R.ok().put("wcsCommandlog", wcsCommandlog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcscommandlog:save")
    public R save(@RequestBody WcsCommandlogEntity wcsCommandlog){
		wcsCommandlogService.save(wcsCommandlog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcscommandlog:update")
    public R update(@RequestBody WcsCommandlogEntity wcsCommandlog){
		wcsCommandlogService.updateById(wcsCommandlog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcscommandlog:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsCommandlogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
