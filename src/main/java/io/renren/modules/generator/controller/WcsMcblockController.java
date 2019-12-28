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

import io.renren.modules.generator.entity.WcsMcblockEntity;
import io.renren.modules.generator.service.WcsMcblockService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 母车数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcsmcblock")
public class WcsMcblockController {
    @Autowired
    private WcsMcblockService wcsMcblockService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsmcblock:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsMcblockService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsmcblock:info")
    public R info(@PathVariable("id") Integer id){
		WcsMcblockEntity wcsMcblock = wcsMcblockService.getById(id);

        return R.ok().put("wcsMcblock", wcsMcblock);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsmcblock:save")
    public R save(@RequestBody WcsMcblockEntity wcsMcblock){
		wcsMcblockService.save(wcsMcblock);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsmcblock:update")
    public R update(@RequestBody WcsMcblockEntity wcsMcblock){
		wcsMcblockService.updateById(wcsMcblock);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsmcblock:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsMcblockService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
