package cn.e3mall.content.service;

import cn.e3.commom.easyUIGridResult.EasyUITreeNode;

import java.util.List;

public interface ContentCatService {

    public List<EasyUITreeNode> getContentCatList(Long parentId);


}
