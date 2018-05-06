package cn.e3mall.controller;

import cn.e3.commom.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;


    /**
     * 将所有商品导入索引库
     * */
    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importAllItem(){
        E3Result e3Result = searchItemService.importAllItems();
        return e3Result;
    }

}
