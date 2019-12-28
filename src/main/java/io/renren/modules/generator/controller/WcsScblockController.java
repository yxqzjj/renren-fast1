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

import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.service.WcsScblockService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 穿梭车数据block表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@RestController
@RequestMapping("generator/wcsscblock")
public class WcsScblockController {
    @Autowired
    private WcsScblockService wcsScblockService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsscblock:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsScblockService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsscblock:info")
    public R info(@PathVariable("id") Integer id){
		WcsScblockEntity wcsScblock = wcsScblockService.getById(id);

        return R.ok().put("wcsScblock", wcsScblock);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsscblock:save")
    public R save(@RequestBody WcsScblockEntity wcsScblock){
		wcsScblockService.save(wcsScblock);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsscblock:update")
    public R update(@RequestBody WcsScblockEntity wcsScblock){
		wcsScblockService.updateById(wcsScblock);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsscblock:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsScblockService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
