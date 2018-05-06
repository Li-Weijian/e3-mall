package cn.e3mall.controller;

import cn.e3.commom.pojo.EasyUIGirdResult;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;


    /**
     * 获取内容列表
     * */
    @RequestMapping("content/query/list")
    @ResponseBody
    public EasyUIGirdResult getContentList(Long categoryId, int page, int rows){
        EasyUIGirdResult result = contentService.getContentList(categoryId, page, rows);
        return result;
    }

    /**
     * 新增内容
     * */
    @RequestMapping("/content/save")
    @ResponseBody
    public E3Result addContent(TbContent content){
        E3Result result = contentService.addContent(content);
        return result;
    }


}
