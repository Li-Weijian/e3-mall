package cn.e3mall.content.service;

import cn.e3.commom.pojo.EasyUIGirdResult;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.pojo.TbContent;

import java.util.List;

public interface ContentService {

    public EasyUIGirdResult getContentList(Long categoryId, int page, int rows);

    public E3Result addContent(TbContent content);

    public List<TbContent> getContentByCid(Long cid);

}

