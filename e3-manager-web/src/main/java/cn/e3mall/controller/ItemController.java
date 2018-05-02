package cn.e3mall.controller;

import cn.e3.commom.easyUIGridResult.EasyUIGirdResult;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    //转跳到请求页面
    @RequestMapping("{page}")
    public String toPage(@PathVariable String page){
        return page;
    }


    //转跳到首页
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable long itemId){
        TbItem item = itemService.getItemById(itemId);
        return item;
    }

    /**
     * 获取商品列表
     * */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIGirdResult getItemList(Integer page, Integer rows){
        EasyUIGirdResult list = itemService.getItemList(page, rows);
        return list;
    }


    /**
     * 添加商品
     * */
    @RequestMapping("/item/save")
    @ResponseBody
    public E3Result addItem(TbItem item, String desc){
        E3Result result = itemService.addItem(item, desc);
        return result;
    }

    /**
     * 更新商品
     * */
    @RequestMapping("/item/update")
    @ResponseBody
    public E3Result updateItem(TbItem item, String desc){
        E3Result result = itemService.updateItem(item, desc);
        return result;
    }

}
