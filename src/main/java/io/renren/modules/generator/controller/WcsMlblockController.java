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

import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.service.WcsMlblockService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 堆垛机数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcsmlblock")
public class WcsMlblockController {
    @Autowired
    private WcsMlblockService wcsMlblockService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsmlblock:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsMlblockService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsmlblock:info")
    public R info(@PathVariable("id") Integer id){
		WcsMlblockEntity wcsMlblock = wcsMlblockService.getById(id);

        return R.ok().put("wcsMlblock", wcsMlblock);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsmlblock:save")
    public R save(@RequestBody WcsMlblockEntity wcsMlblock){
		wcsMlblockService.save(wcsMlblock);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsmlblock:update")
    public R update(@RequestBody WcsMlblockEntity wcsMlblock){
		wcsMlblockService.updateById(wcsMlblock);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsmlblock:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsMlblockService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
