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

import io.renren.modules.generator.entity.WcsWarehouseEntity;
import io.renren.modules.generator.service.WcsWarehouseService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 仓库库区表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@RestController
@RequestMapping("generator/wcswarehouse")
public class WcsWarehouseController {
    @Autowired
    private WcsWarehouseService wcsWarehouseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcswarehouse:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsWarehouseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcswarehouse:info")
    public R info(@PathVariable("id") Integer id){
		WcsWarehouseEntity wcsWarehouse = wcsWarehouseService.getById(id);

        return R.ok().put("wcsWarehouse", wcsWarehouse);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcswarehouse:save")
    public R save(@RequestBody WcsWarehouseEntity wcsWarehouse){
		wcsWarehouseService.save(wcsWarehouse);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcswarehouse:update")
    public R update(@RequestBody WcsWarehouseEntity wcsWarehouse){
		wcsWarehouseService.updateById(wcsWarehouse);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcswarehouse:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsWarehouseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
