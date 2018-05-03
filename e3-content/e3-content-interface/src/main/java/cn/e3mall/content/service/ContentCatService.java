package cn.e3mall.content.service;

import cn.e3.commom.easyUIGridResult.EasyUITreeNode;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.pojo.TbContentCategory;

import java.util.List;

public interface ContentCatService {

    public List<EasyUITreeNode> getContentCatList(Long parentId);

    public E3Result createContentCatNode(TbContentCategory contentCategory);

}
