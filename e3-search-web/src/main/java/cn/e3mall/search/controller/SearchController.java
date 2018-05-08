package cn.e3mall.search.controller;

import cn.e3.commom.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_ITEM_ROWS}")
    private int SEARCH_ITEM_ROWS;

    @RequestMapping("/search")
    public String searchItemList(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {

        keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
        SearchResult result = searchService.search(page, SEARCH_ITEM_ROWS, keyword);
        model.addAttribute("itemList",result.getItemList());
        model.addAttribute("recourdCount",result.getRecourdCount());
        model.addAttribute("page",page);
        model.addAttribute("totalPages",result.getTotalPages());
        model.addAttribute("query",keyword);


        return "search";
    }



}

