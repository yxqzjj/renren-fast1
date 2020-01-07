package io.renren.modules.generator.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.wap.dao.*;
import io.renren.wap.entity.constant.MachineConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 设备监控
 *
 * @Author: CalmLake
 * @Date: 2019/3/18  14:51
 * @Version: V1.0.0
 **/
@Controller
@RequestMapping("monitorControl")
public class MonitorControl {

    @Resource(name = "ALBlockDao")
    ALBlockDao alBlockDao;
    @Resource(name = "CLBlockDao")
    CLBlockDao clBlockDao;
    @Resource(name = "RGVBlockDao")
    RGVBlockDao rgvBlockDao;
    @Resource(name = "SCBlockDao")
    SCBlockDao scBlockDao;
    @Resource(name = "MCBlockDao")
    MCBlockDao mcBlockDao;
    @Resource(name = "MLBlockDao")
    MLBlockDao mlBlockDao;

    @RequestMapping("getBlockStatus")
    @ResponseBody
    public JSONArray getBlockStatus(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        String data = request.getParameter("data");
        String[] blockName = data.split("&");
        List<WcsClblockEntity> clBlockList = clBlockDao.getClBlockList();
        List<WcsMlblockEntity> mlBlockList = mlBlockDao.getMlBlockList();
        List<WcsScblockEntity> scBlockList = scBlockDao.getSCBlockList();
        for (WcsClblockEntity clBlock : clBlockList) {
            String blockNameStr = clBlock.getName();
            for (String strName : blockName) {
                if (blockNameStr.equals(strName)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", strName);
                    jsonObject.put("command", clBlock.getCommand());
                    jsonObject.put("load", clBlock.getIsLoad());
                    jsonObject.put("errorCode", clBlock.getErrorCode());
                    jsonObject.put("status", clBlock.getStatus());
                    jsonObject.put("use", true);
                    jsonArray.add(jsonObject);
                }
            }
        }
        for (WcsMlblockEntity mlBlock : mlBlockList) {
            String blockNameStr = mlBlock.getName();
            for (String strName : blockName) {
                if (blockNameStr.equals(strName)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", strName);
                    jsonObject.put("type", MachineConstant.BYTE_TYPE_ML);
                    jsonObject.put("scName", mlBlock.getScBlockName());
                    jsonObject.put("berthBlockName", mlBlock.getBerthBlockName());
                    jsonObject.put("command", mlBlock.getCommand());
                    jsonObject.put("load", mlBlock.getIsLoad());
                    jsonObject.put("errorCode", mlBlock.getErrorCode());
                    jsonObject.put("status", mlBlock.getStatus());
                    jsonObject.put("use", true);
                    jsonArray.add(jsonObject);
                }
            }
        }
        for (WcsScblockEntity scBlock : scBlockList) {
            String blockNameStr = scBlock.getName();
            for (String strName : blockName) {
                if (blockNameStr.equals(strName)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", strName);
                    jsonObject.put("type", MachineConstant.BYTE_TYPE_SC);
                    jsonObject.put("hostName", scBlock.getHostBlockName());
                    jsonObject.put("command", scBlock.getCommand());
                    jsonObject.put("load", scBlock.getIsLoad());
                    jsonObject.put("status", scBlock.getStatus());
                    jsonObject.put("errorCode", scBlock.getErrorCode());
                    jsonObject.put("standbyCar", scBlock.getIsStandbyCar());
                    jsonObject.put("use", scBlock.getIsUse());
                    jsonArray.add(jsonObject);
                }
            }
        }
        return jsonArray;
    }

}
