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

import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.modules.generator.service.WcsPlcconfigService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * plc配置表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcsplcconfig")
public class WcsPlcconfigController {
    @Autowired
    private WcsPlcconfigService wcsPlcconfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsplcconfig:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsPlcconfigService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsplcconfig:info")
    public R info(@PathVariable("id") Integer id){
		WcsPlcconfigEntity wcsPlcconfig = wcsPlcconfigService.getById(id);

        return R.ok().put("wcsPlcconfig", wcsPlcconfig);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsplcconfig:save")
    public R save(@RequestBody WcsPlcconfigEntity wcsPlcconfig){
		wcsPlcconfigService.save(wcsPlcconfig);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsplcconfig:update")
    public R update(@RequestBody WcsPlcconfigEntity wcsPlcconfig){
		wcsPlcconfigService.updateById(wcsPlcconfig);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsplcconfig:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsPlcconfigService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
