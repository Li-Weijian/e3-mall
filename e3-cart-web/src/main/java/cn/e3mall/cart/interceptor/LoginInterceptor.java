package cn.e3mall.cart.interceptor;

import cn.e3.commom.utils.CookieUtils;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录状态拦截器
 * */
public class LoginInterceptor implements HandlerInterceptor {


    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;
    @Autowired
    private TokenService tokenService;

    //handle处理之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handle) throws Exception {
        //1. 从cookie中取出token
        String token = CookieUtils.getCookieValue(httpServletRequest, COOKIE_TOKEN_KEY, true);
        //2. 取不到，未登录
        if (StringUtils.isBlank(token)){
            return true;    //直接放行，让用户将购物车信息存储到cookie中
        }
        //3. 取得到，去redis中查询是否过期
        E3Result result = tokenService.getUserByToken(token);
        if (result.getStatus() != 200){
            //4. 过期，未登录
            return true;
        }
        //5. 否则，已登录
        TbUser user  = (TbUser) result.getData();
        httpServletRequest.setAttribute("user",user);       //放入request中作为登录的凭证
        return true;
    }

    //handle处理之后，返回参数之前
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handle, ModelAndView modelAndView) throws Exception {

    }

    //返回参数之后
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handle, Exception e) throws Exception {

    }
}
