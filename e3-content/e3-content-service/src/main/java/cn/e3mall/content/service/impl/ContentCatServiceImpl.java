package cn.e3mall.content.service.impl;

import cn.e3.commom.pojo.EasyUITreeNode;
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

    /**
     * 更新分类节点
     * */
    @Override
    public E3Result updateContentCatNode(Long id, String name) {

        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setId(id);
        contentCategory.setName(name);

        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteContentCatNode(Long id) {
        TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
        //1. 判断是否为父节点，如果是，不允许删除
        if (category.getIsParent()){
            return E3Result.build(500,"父节点不允许删除，如需删除，必须清空所有子节点");
        }else {
            Long parentId = category.getParentId();
            contentCategoryMapper.deleteByPrimaryKey(id);

            //2. 删除后判断是否有同级节点，如果没有，将父节点的isParent设置为false
            TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = contentCategoryExample.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            int count = contentCategoryMapper.countByExample(contentCategoryExample);
            if (count == 0){
                TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
                contentCategory.setIsParent(false);
                contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
            }
        }
        return E3Result.ok();
    }
}
