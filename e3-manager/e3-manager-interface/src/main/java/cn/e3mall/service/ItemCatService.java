package cn.e3mall.service;

import cn.e3.commom.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {

    /**
     * 获取商品类目
     * */
    public List<EasyUITreeNode> getItemCat(long parentId);



}
