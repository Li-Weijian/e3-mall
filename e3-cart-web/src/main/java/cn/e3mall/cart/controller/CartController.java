package cn.e3mall.cart.controller;


import cn.e3.commom.utils.CookieUtils;
import cn.e3.commom.utils.E3Result;
import cn.e3.commom.utils.JsonUtils;
import cn.e3mall.cart.service.CartService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ItemService itemService;

   @Autowired
    private CartService cartService;

    /**
     * 添加商品到购物车
     * */
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, int num, HttpServletRequest request, HttpServletResponse response){
        //取出request中的user
        Object o = request.getAttribute("user");
        //如果有，已登录状态
        if (o != null){
            TbUser user = (TbUser) o;
            E3Result result = cartService.addCart(user.getId().intValue(),itemId, num);
            return "cartSuccess";
        }

        //1.根据itemId查询cookie中是否存在该商品
        List<TbItem> cartList = getCartList(request);
        boolean flat = false;       //商品是否存在的标志

        //判断商品是否在cookie中存在
        for (TbItem item:cartList) {
            if (item.getId() == itemId.longValue()){
                //2.如果存在，则将商品数量累加
                item.setNum(item.getNum() + num);
                flat = true;
                break;
            }
        }
        //3.如果不存在，去数据库中查询该商品
        if (!flat){
            TbItem item = itemService.getItemById(itemId);
            //4.1设置数量
            item.setNum(num);
            //4.2取第一张图片
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)){
                item.setImage(image.split(",")[0]);
            }
            //4.3添加到列表
            cartList.add(item);
        }

        //4.4设置cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),true);
        //5.返回逻辑视图：添加商品成功页面
        return "cartSuccess";
    }


    /**
     * 展示购物车列表
     * */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request, HttpServletResponse response, Model model){

        //1. 获取cookie中的商品列表
        List<TbItem> cartList = getCartList(request);
        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登录状态
        if (user != null) {
            //从cookie中取购物车列表
            //如果不为空，把cookie中的购物车商品和服务端的购物车商品合并。
            cartService.mergeCart(user.getId(), cartList);
            //把cookie中的购物车删除
            CookieUtils.deleteCookie(request, response, "cart");
            //从服务端取购物车列表
            cartList = cartService.getCartList(user.getId());

        }
        //2. 添加到model中
        model.addAttribute("cartList",cartList);
        //3. 返回逻辑视图:cart
        return "cart";
    }


    /**
     * 更新购物车数量
     * */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCastNum(@PathVariable Long itemId, @PathVariable int num,HttpServletResponse response, HttpServletRequest request){
        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.updateCartNum(user.getId(), itemId, num);
            return E3Result.ok();
        }
        //1. 从cookie中取出商品列表
        List<TbItem> cartList = getCartList(request);
        //2. 遍历列表找到对应商品
        for (TbItem item: cartList) {
            if (item.getId() == itemId.longValue()){
                //3. 更新商品数量
                item.setNum(num);
                break;
            }
        }
        //4. 重新写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),true);
        //5. 返回E3Result
        return E3Result.ok();
    }


    /**
     * 删除购物车商品
     * */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCart(@PathVariable Long itemId,HttpServletRequest request, HttpServletResponse response){
        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
        //1. 从cookie中获取购物车列表
        List<TbItem> cartList = getCartList(request);
        //2. 遍历列表找到对应商品
        for (TbItem item: cartList) {
            if (item.getId() == itemId.longValue()){
                //3. 删除该商品
                cartList.remove(item);
                break;
            }
        }
        //4. 重新写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),true);
        //5. 重定向到/cart/cart.html
        return "redirect:/cart/cart.html";
    }


    /**
     * 获取cookie中的购物车列表
     * */
    public List<TbItem> getCartList(HttpServletRequest request){
        //从cookie中获取购物车列表
        String json = CookieUtils.getCookieValue(request, "cart", true);

        if (StringUtils.isNotBlank(json)){
            //如果不为空，则转换成商品列表对象
            List<TbItem> list = JsonUtils.jsonToList(json,TbItem.class);
            return list;
        }
        return new ArrayList<>();
    }


}
