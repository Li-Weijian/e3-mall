package cn.e3mall.sso.controller;

import cn.e3.commom.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;


    @RequestMapping("/user/check/{data}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String data, @PathVariable Integer type){
        E3Result e3Result = registerService.checkData(data, type);
        return e3Result;
    }

    @RequestMapping("/user/register")
    @ResponseBody
    public E3Result register(TbUser user){
        E3Result result = registerService.register(user);
        return result;
    }


}
