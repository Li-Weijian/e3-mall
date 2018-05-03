package cn.e3mall.content.service.impl;

import cn.e3.commom.easyUIGridResult.EasyUITreeNode;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCatServiceImpl implements ContentCatService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;


    /**
     * 获取内容分类列表
     * */
    @Override
    public List<EasyUITreeNode> getContentCatList(Long parentId) {

        TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = contentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(contentCategoryExample);

        List<EasyUITreeNode> list = new ArrayList<>();
        for (TbContentCategory category:contentCategoryList) {
            EasyUITreeNode treeNode = new EasyUITreeNode();
            treeNode.setId(category.getId());
            treeNode.setText(category.getName());
            treeNode.setState(category.getIsParent() == true ? "closed":"open");
            list.add(treeNode);
        }
        return list;
    }


    /**
     * 创建内容分类
     * */
    @Override
    public E3Result createContentCatNode(TbContentCategory contentCategory) {

        //状态。可选值:1(正常),2(删除)
        contentCategory.setStatus(1);
        //排列序号，都是1
        contentCategory.setSortOrder(1);
        //所有新建的都是叶子节点
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());

        //插入节点
        contentCategoryMapper.insert(contentCategory);

        TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
        //判断父节点idParent是否为true，如果不是则更新为true
        if (!parentNode.getIsParent()){
            parentNode.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        return E3Result.ok(contentCategory);
    }
}
