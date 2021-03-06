package cn.e3mall.controller;

import cn.e3.commom.pojo.EasyUITreeNode;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCatController {

    @Autowired
    private ContentCatService contentCatService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(value = "id",defaultValue = "0") Long parentId){
        List<EasyUITreeNode> list = contentCatService.getContentCatList(parentId);
        return list;
    }

    @RequestMapping("/content/category/create")
    @ResponseBody
    public E3Result createContentCatNode(TbContentCategory contentCategory){
        E3Result result = contentCatService.createContentCatNode(contentCategory);
        return result;
    }


    @RequestMapping("/content/category/update")
    @ResponseBody
    public E3Result updateContentCatNode(Long id, String name){
        E3Result result = contentCatService.updateContentCatNode(id, name);
        return result;
    }

    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public E3Result deleteContentCatNode(Long id){
        E3Result result = contentCatService.deleteContentCatNode(id);
        return result;
    }


}
