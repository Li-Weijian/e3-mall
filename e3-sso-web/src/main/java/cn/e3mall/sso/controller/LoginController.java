package cn.e3mall.sso.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/page/{page}")
    public String toLogin(@PathVariable String page){

        return page;
    }



}
