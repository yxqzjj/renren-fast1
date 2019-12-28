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

import io.renren.modules.generator.entity.WcsCrossrouteEntity;
import io.renren.modules.generator.service.WcsCrossrouteService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 交叉路径信息
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@RestController
@RequestMapping("generator/wcscrossroute")
public class WcsCrossrouteController {
    @Autowired
    private WcsCrossrouteService wcsCrossrouteService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcscrossroute:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsCrossrouteService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcscrossroute:info")
    public R info(@PathVariable("id") Integer id){
		WcsCrossrouteEntity wcsCrossroute = wcsCrossrouteService.getById(id);

        return R.ok().put("wcsCrossroute", wcsCrossroute);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcscrossroute:save")
    public R save(@RequestBody WcsCrossrouteEntity wcsCrossroute){
		wcsCrossrouteService.save(wcsCrossroute);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcscrossroute:update")
    public R update(@RequestBody WcsCrossrouteEntity wcsCrossroute){
		wcsCrossrouteService.updateById(wcsCrossroute);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcscrossroute:delete")
    public R delete(@RequestBody Integer[] ids){
		wcsCrossrouteService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
