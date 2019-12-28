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

import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.modules.generator.service.WcsAlblockService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 升降机数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcsalblock")
public class WcsAlblockController {
    @Autowired
    private WcsAlblockService wcsAlblockService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsalblock:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsAlblockService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsalblock:info")
    public R info(@PathVariable("id") Integer id){
		WcsAlblockEntity wcsAlblock = wcsAlblockService.getById(id);

        return R.ok().put("wcsAlblock", wcsAlblock);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsalblock:save")
    public R save(@RequestBody WcsAlblockEntity wcsAlblock){
		wcsAlblockService.save(wcsAlblock);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsalblock:update")
    public R update(@RequestBody WcsAlblockEntity wcsAlblock){
		wcsAlblockService.updateById(wcsAlblock);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsalblock:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsAlblockService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
