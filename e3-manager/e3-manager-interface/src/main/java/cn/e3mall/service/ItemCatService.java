package cn.e3mall.service;

import cn.e3.commom.easyUIGridResult.EasyUITreeNode;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.pojo.TbContentCategory;

import java.util.List;

public interface ItemCatService {

    /**
     * 获取商品类目
     * */
    public List<EasyUITreeNode> getItemCat(long parentId);



}
