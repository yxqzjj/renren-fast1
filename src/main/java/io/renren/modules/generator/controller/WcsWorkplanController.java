package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsRouteStationStartEndDaoImpl;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.entity.WcsCommandlogEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.entity.constant.WMSMessageLogConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.service.CreateXmlService;
import io.renren.wap.server.xml.constant.XmlInfoConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.service.AssigningTaskService;
import io.renren.wap.service.WmsMessageLogService;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.modules.generator.service.WcsWorkplanService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 工作计划表
 *
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:10
 */
@RestController
@RequestMapping("generator/wcsworkplan")
public class WcsWorkplanController {
    @Autowired
    private WcsWorkplanService wcsWorkplanService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wcsworkplan:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wcsWorkplanService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:wcsworkplan:info")
    public R info(@PathVariable("id") Integer id){
		WcsWorkplanEntity wcsWorkplan = wcsWorkplanService.getById(id);

        return R.ok().put("wcsWorkplan", wcsWorkplan);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wcsworkplan:save")
    public R save(@RequestBody WcsWorkplanEntity wcsWorkplan){
        String msg;
        int code=1;
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==wcsWorkplan.getType() || WorkPlanConstant.TYPE_OUT_PUT_STORAGE==wcsWorkplan.getType()) {
            int num = WcsRouteStationStartEndDaoImpl.getRouteStationStartEndDao().countNumByFromStationAndEndStation( wcsWorkplan.getFromStation(),wcsWorkplan.getToStation(),wcsWorkplan.getType());
            if (num > 0) {
                WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan(wcsWorkplan.getMckey(),
                        wcsWorkplan.getWmsFlag(), wcsWorkplan.getToLocation(), wcsWorkplan.getToStation(),
                        wcsWorkplan.getBarcode(), wcsWorkplan.getType(), wcsWorkplan.getFromStation(),
                        wcsWorkplan.getFromLocation());
                AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
                assigningTaskService.assigningTasks();
                code=0;
                msg = "创建工作计划成功！噜噜噜";
            } else {
                code=1;
                msg = "创建工作计划失败，不是有效路径！怕不是遇到了..";

            }
        } else {

            WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan(wcsWorkplan.getMckey(),
                    wcsWorkplan.getWmsFlag(), wcsWorkplan.getToLocation(), wcsWorkplan.getToStation(),
                    wcsWorkplan.getBarcode(), wcsWorkplan.getType(), wcsWorkplan.getFromStation(),
                    wcsWorkplan.getFromLocation());
            AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
            assigningTaskService.assigningTasks();
            code=0;
            msg = "创建工作计划成功！噜噜噜";

        }
        return R.error(code,msg);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wcsworkplan:update")
    public R update(@RequestBody WcsWorkplanEntity wcsWorkplan){
		wcsWorkplanService.updateById(wcsWorkplan);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wcsworkplan:delete")
    public R delete(@RequestBody Integer[] ids){
        List<Integer> list=Arrays.asList(ids);
        int i=0;
        for (Integer idInt:list) {
            WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectById(idInt);
            String mcKey = workPlan.getMckey();
            workPlan.setStatus(WorkPlanConstant.STATUS_CANCEL);
            DbUtil.getWorkPlanDao().updateById(workPlan);
            DbUtil.getCommandLogDao().delete(new QueryWrapper<WcsCommandlogEntity>().eq("McKey",mcKey));
            DbUtil.getTaskingDao().delete(new QueryWrapper<WcsTaskingEntity>().eq("McKey",mcKey));
            WorkPlanService.createWorkPlanLog(idInt);
             i = DbUtil.getWorkPlanDao().deleteById(idInt);
            List<WcsClblockEntity> list1=DbUtil.getCLBlockDao().selectList(new QueryWrapper<WcsClblockEntity>().eq("Mckey",mcKey));
            for (WcsClblockEntity wcsClblockEntity:list1 ) {
                wcsClblockEntity.setMckey("");
                wcsClblockEntity.setWithWorkBlockName("");
                DbUtil.getCLBlockDao().updateById(wcsClblockEntity);
            }

        }
        if (i > 0) {
            return R.error(0,"删除工作计划成功");
        } else {
            return  R.error(1,"失败");
        }

    }

    /**
     * 完成
     */
    @RequestMapping("/finishWorkPlan")
    @RequiresPermissions("generator:wcsworkplan:finishWorkPlan")
    public R finishWorkPlan(@RequestBody Integer id) {
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectById(id);
        String mcKey = workPlan.getMckey();
        workPlan.setFinishTime(new Date());
        workPlan.setStatus(WorkPlanConstant.STATUS_FINISH);
        int workPlanType = workPlan.getType();
        int i = DbUtil.getWorkPlanDao().updateById(workPlan);
        ;
        if (i > 0) {
            DbUtil.getCommandLogDao().delete(new QueryWrapper<WcsCommandlogEntity>().eq("Mckey", mcKey));
            DbUtil.getTaskingDao().delete(new QueryWrapper<WcsTaskingEntity>().eq("McKey", mcKey));
            WorkPlanService.createWorkPlanLog(id);
            DbUtil.getWorkPlanDao().deleteById(id);
             R.error(0, "成功");
            if (WorkPlanConstant.TYPE_PUT_IN_STORAGE == workPlanType || WorkPlanConstant.TYPE_OUT_PUT_STORAGE == workPlanType || WorkPlanConstant.TYPE_MOVEMENT == workPlanType || WorkPlanConstant.TYPE_TALLY == workPlanType || WorkPlanConstant.TYPE_TAKE_STOCK == workPlanType) {
                try {
                    EnvelopeDTO envelopeDTO=CreateXmlService.createMovementReport
                            (workPlan.getBarcode(), workPlan.getWmsFlag(),
                                    workPlan.getFromStation(), workPlan.getFromLocation(),
                                    workPlan.getToStation(), workPlan.getToLocation(),
                                    XmlInfoConstant.XML_DEFAULT_00, XmlInfoConstant.XML_DEFAULT_00);
                    WmsMessageLogService wmsMessageLogService = new WmsMessageLogService();
                    wmsMessageLogService.insertWMSMessageLog(workPlan.getWmsFlag(), workPlan.getBarcode(), workPlan.getId(), envelopeDTO, WMSMessageLogConstant.TYPE_MOVEMENTREPORT, WMSMessageLogConstant.STATUS_SEND, "");
                    XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, envelopeDTO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return R.error(1, "失败");
                }
            }
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工完成工作计划，结果：%s", id, R.ok()));

        return R.error(0, "成功");
    }
}
