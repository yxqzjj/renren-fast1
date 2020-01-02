package io.renren.wap.control;

import com.alibaba.fastjson.JSONObject;
import io.renren.modules.generator.dao.impl.UserDaoImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户控制
 *
 * @Author: CalmLake
 * @Date: 2019/2/19  14:50
 * @Version: V1.0.0
 **/
@Controller
@RequestMapping("userControl")
public class UserControl {

    @RequestMapping("loginOut")
    @ResponseBody
    public String loginOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionName = "userName";
        boolean result = false;
        if (session.getAttribute(sessionName) != null) {
            session.removeAttribute(sessionName);
            result = true;
        }
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("result", result);
        return jsonObject1.toJSONString();
    }

    @RequestMapping("getUserName")
    @ResponseBody
    public String getUserName(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionName = "userName";
        boolean result = false;
        String userName = "";
        if (session.getAttribute(sessionName) != null) {
            userName = session.getAttribute(sessionName).toString();
            result = true;
        }
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("result", result);
        jsonObject1.put("userName", userName);
        return jsonObject1.toJSONString();
    }

    @RequestMapping("addNewUser")
    @ResponseBody
    public String addNewUser(HttpServletRequest request) {
        String data = request.getParameter("msg");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String name = jsonObject.getString("name");
        String password1 = jsonObject.getString("password1");
        String password2 = jsonObject.getString("password2");
        int num = UserDaoImpl.getUserDao().countByName(name);
        boolean result;
        String code;
        String msg;
        if (num > 0) {
            code = "001";
            msg = "该用户名已存在";
            result = false;
        } else {
            if (password1.equals(password2)) {
                UserDaoImpl.getUserDao().insertUserNamePassword(name, password1);
                code = "000";
                msg = "创建成功";
                result = true;
            } else {
                code = "002";
                msg = "两次密码不同";
                result = false;
            }
        }
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("result", result);
        jsonObject1.put("code", code);
        jsonObject1.put("msg", msg);
        return jsonObject1.toJSONString();
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public String updateUser(HttpServletRequest request) {
        String data = request.getParameter("msg");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        int i = UserDaoImpl.getUserDao().updatePasswordByName(name, password);
        JSONObject jsonObject1 = new JSONObject();
        if (i > 0) {
            jsonObject1.put("result", true);
        } else {
            jsonObject1.put("result", false);
        }
        return jsonObject1.toJSONString();
    }
}
