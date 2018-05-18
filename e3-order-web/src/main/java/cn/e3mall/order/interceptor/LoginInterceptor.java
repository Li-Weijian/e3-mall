package cn.e3mall.order.interceptor;

import cn.e3.commom.utils.CookieUtils;
import cn.e3.commom.utils.E3Result;
import cn.e3.commom.utils.JsonUtils;
import cn.e3mall.cart.service.CartService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 付款之前的登录拦截器
 * */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;

    @Value("${REDIRECT_URL}")
    private String REDIRECT_URL;
    @Value("${ORDER_REDIRECT_URL}")
    private String ORDER_REDIRECT_URL;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

        //从cookie中取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        //如果取不到，未登录，跳转到登录页面
        if (StringUtils.isBlank(token)){
            httpServletResponse.sendRedirect(REDIRECT_URL + "/page/login?redirect="+ORDER_REDIRECT_URL+httpServletRequest.getRequestURI());
            return false;
        }
        //如果取到，调用sso服务查询redis判断登录是否过期
        E3Result e3Result = tokenService.getUserByToken(token);
        if (e3Result.getStatus() != 200){
            //过期，拦截，跳转到登录页面
            httpServletResponse.sendRedirect(REDIRECT_URL + "/page/login?redirect="+ORDER_REDIRECT_URL+httpServletRequest.getRequestURI());
            return false;
        }
        //没过期，已登录，将用户存入request，放行
        TbUser tbUser = (TbUser) e3Result.getData();
        httpServletRequest.setAttribute("user",tbUser);

        //合并购物车到服务端
        String cookieValue = CookieUtils.getCookieValue(httpServletRequest, "cart", true);
        if (StringUtils.isNotBlank(cookieValue)){
            cartService.mergeCart(tbUser.getId(),JsonUtils.jsonToList(cookieValue,TbItem.class));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
