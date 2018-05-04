package cn.e3mall.controller;

import cn.e3.commom.easyUIGridResult.EasyUIGirdResult;
import cn.e3mall.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;


    @RequestMapping("content/query/list")
    @ResponseBody
    public EasyUIGirdResult getContentList(Long categoryId, int page, int rows){
        EasyUIGirdResult result = contentService.getContentList(categoryId, page, rows);
        return result;
    }

}
