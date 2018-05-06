package cn.e3mall.service.impl;

import cn.e3.commom.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;


    @Override
    public List<EasyUITreeNode> getItemCat(long parentId) {

        TbItemCatExample itemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = itemCatExample.createCriteria();
        //根据父节点id进行查询
        criteria.andParentIdEqualTo(parentId);

        List<TbItemCat> itemCatList = itemCatMapper.selectByExample(itemCatExample);
        List<EasyUITreeNode> treeNodes = new ArrayList<>();
        for (TbItemCat cat: itemCatList) {
            EasyUITreeNode treeNode = new EasyUITreeNode();
            treeNode.setId(cat.getId());
            treeNode.setText(cat.getName());
            //如果还有子节点，将不展开。
            treeNode.setState(cat.getIsParent() == true ? "closed":"open");
            treeNodes.add(treeNode);
        }

        return treeNodes;
    }
}
