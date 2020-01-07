package io.renren.modules.generator.controller;

import com.alibaba.fastjson.JSONObject;
import io.renren.modules.generator.entity.WcsUserEntity;
import io.renren.wap.dao.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 页面跳转控制
 *
 * @Author: CalmLake
 * @Date: 2019/2/18  14:57
 * @Version: V1.0.0
 **/
@Controller
@RequestMapping("pageControl")
public class PageControl {
    @Resource(name = "UserDao")
    private UserDao userDao;

    @RequestMapping("/wmsMessage")
    public String wmsMessageHtml() {
        return "wmsMessage";
    }

    @RequestMapping("/monitor")
    public String monitorHtml() {
        return "monitor";
    }

    @RequestMapping("/createWorkPlan")
    public String createWorkPlanHtml() {
        return "createWorkPlan";
    }

    @RequestMapping("/handMessage")
    public String handMessageHtml() {
        return "handMessage";
    }

    @RequestMapping("/workPlan")
    public String workPlanHtml() {
        return "workPlan";
    }

    @RequestMapping("/message")
    public String messageHtml() {
        return "message";
    }

    @RequestMapping("/block")
    public String blockHtml() {
        return "block";
    }

    @RequestMapping("/machine")
    public String machineHtml() {
        return "machine";
    }

    @RequestMapping("/route")
    public String routeHtml() {
        return "route";
    }

    @RequestMapping("/show404")
    public String errorHtml() {
        return "404";
    }

    @RequestMapping("/index")
    public String indexHtml() {
        return "index";
    }

    @RequestMapping("/login")
    public String loginHtml(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionName = "userName";
        boolean result = false;
        if (session.getAttribute(sessionName) != null) {
            result = true;
        }
        if (result){
            return "index";
        }else {
            return "login";
        }
    }

    @RequestMapping("/register")
    public String registerHtml() {
        return "register";
    }

    @RequestMapping("/forgot-password")
    public String forgotPasswordHtml() {
        return "forgot-password";
    }

    @RequestMapping("/loginIndex")
    @ResponseBody
    public String loginIndexHtml(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String data = request.getParameter("msg");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        WcsUserEntity user = userDao.selectByName(name);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("result", false);
        if (user != null) {
            if (user.getName().equals(name)) {
                if (user.getPassword().equals(password)) {
                    String sessionName = "userName";
                    if (session.getAttribute(sessionName) == null) {
                        session.setAttribute(sessionName, name);
                    }
                    jsonObject1.put("result", true);
                }
            }
        }
        return jsonObject1.toJSONString();
    }
}
