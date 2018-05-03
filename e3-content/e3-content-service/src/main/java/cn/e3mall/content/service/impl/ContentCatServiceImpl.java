package cn.e3mall.content.service.impl;

import cn.e3.commom.easyUIGridResult.EasyUITreeNode;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
