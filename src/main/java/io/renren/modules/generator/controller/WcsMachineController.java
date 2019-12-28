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

import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.service.WcsMachineService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 设备信息表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcsmachine")
public class WcsMachineController {
    @Autowired
    private WcsMachineService wcsMachineService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsmachine:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsMachineService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsmachine:info")
    public R info(@PathVariable("id") Integer id){
		WcsMachineEntity wcsMachine = wcsMachineService.getById(id);

        return R.ok().put("wcsMachine", wcsMachine);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsmachine:save")
    public R save(@RequestBody WcsMachineEntity wcsMachine){
		wcsMachineService.save(wcsMachine);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsmachine:update")
    public R update(@RequestBody WcsMachineEntity wcsMachine){
		wcsMachineService.updateById(wcsMachine);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsmachine:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsMachineService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
