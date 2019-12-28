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

import io.renren.modules.generator.entity.WcsPriorityconfigEntity;
import io.renren.modules.generator.service.WcsPriorityconfigService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 优先级配置表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@RestController
@RequestMapping("generator/wcspriorityconfig")
public class WcsPriorityconfigController {
    @Autowired
    private WcsPriorityconfigService wcsPriorityconfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcspriorityconfig:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsPriorityconfigService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcspriorityconfig:info")
    public R info(@PathVariable("id") Integer id){
		WcsPriorityconfigEntity wcsPriorityconfig = wcsPriorityconfigService.getById(id);

        return R.ok().put("wcsPriorityconfig", wcsPriorityconfig);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcspriorityconfig:save")
    public R save(@RequestBody WcsPriorityconfigEntity wcsPriorityconfig){
		wcsPriorityconfigService.save(wcsPriorityconfig);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcspriorityconfig:update")
    public R update(@RequestBody WcsPriorityconfigEntity wcsPriorityconfig){
		wcsPriorityconfigService.updateById(wcsPriorityconfig);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcspriorityconfig:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsPriorityconfigService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
