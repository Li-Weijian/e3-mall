package cn.e3mall.sso.controller;


import cn.e3.commom.utils.CookieUtils;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/page/{page}")
    public String toLogin(@PathVariable String page){

        return page;
    }

    @RequestMapping("/user/login")
    @ResponseBody
    public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response){

        //调用方法进行校验用户
        E3Result result = loginService.login(username, password);
        //获取token
        String token = result.getData().toString();
        //设置cookie
        CookieUtils.setCookie(request,response,"token",token);
        //返回数据
        return result;
    }



}
