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

import io.renren.modules.generator.entity.WcsErrorcodeEntity;
import io.renren.modules.generator.service.WcsErrorcodeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 故障字典表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcserrorcode")
public class WcsErrorcodeController {
    @Autowired
    private WcsErrorcodeService wcsErrorcodeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcserrorcode:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsErrorcodeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcserrorcode:info")
    public R info(@PathVariable("id") Integer id){
		WcsErrorcodeEntity wcsErrorcode = wcsErrorcodeService.getById(id);

        return R.ok().put("wcsErrorcode", wcsErrorcode);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcserrorcode:save")
    public R save(@RequestBody WcsErrorcodeEntity wcsErrorcode){
		wcsErrorcodeService.save(wcsErrorcode);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcserrorcode:update")
    public R update(@RequestBody WcsErrorcodeEntity wcsErrorcode){
		wcsErrorcodeService.updateById(wcsErrorcode);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcserrorcode:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsErrorcodeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
