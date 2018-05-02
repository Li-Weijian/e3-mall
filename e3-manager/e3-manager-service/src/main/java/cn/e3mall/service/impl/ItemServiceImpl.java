package cn.e3mall.service.impl;

import cn.e3.commom.easyUIGridResult.EasyUIGirdResult;
import cn.e3.commom.utils.E3Result;
import cn.e3.commom.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Override
    public TbItem getItemById(long itemId) {
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        return item;
    }

    /**
     * 获取分页商品列表
     * */
    @Override
    public EasyUIGirdResult getItemList(int page, int rows) {
        //设置分页
        PageHelper.startPage(page,rows);
        //执行查询
        List<TbItem> list = itemMapper.selectByExample(new TbItemExample());
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIGirdResult result = new EasyUIGirdResult(pageInfo.getTotal(),list);
        return result;
    }

    /**
     * 商品添加
     * */
    @Override
    public E3Result addItem(TbItem item, String desc) {
//        1、生成商品id
            long id = IDUtils.genItemId();
//        2、补全TbItem对象的属性
            item.setId(id);
            item.setStatus((byte) 1);
            item.setCreated(new Date());
            item.setUpdated(new Date());
//        3、向商品表插入数据
            itemMapper.insert(item);
//        4、创建一个TbItemDesc对象
            TbItemDesc itemDesc = new TbItemDesc();
//        5、补全TbItemDesc的属性
            itemDesc.setItemDesc(desc);
            itemDesc.setItemId(id);
            itemDesc.setUpdated(new Date());
            itemDesc.setCreated(new Date());
//        6、向商品描述表插入数据
            itemDescMapper.insert(itemDesc);
//        7、E3Result.ok()
            return E3Result.ok();
    }

    /**
     * 更新商品
     * */
    @Override
    public E3Result updateItem(TbItem item, String desc) {
        TbItem tbItem = itemMapper.selectByPrimaryKey(item.getId());
        item.setCreated(tbItem.getCreated());
        item.setUpdated(new Date());
        item.setStatus((byte) 1);
        itemMapper.updateByPrimaryKey(item);

        if (null != desc && !"".equals(desc)){
            TbItemDesc itemDesc = new TbItemDesc();
            itemDesc.setItemId(item.getId());
            itemDesc.setItemDesc(desc);
            itemDesc.setUpdated(new Date());
            itemDescMapper.updateByPrimaryKey(itemDesc);
        }
        return E3Result.ok();
    }

    /**
     * 删除商品
     * */
    @Override
    public E3Result deleteItem(String ids) {
        if (!ids.contains(",")){
            itemMapper.deleteByPrimaryKey(Long.parseLong(ids));
            return E3Result.ok();
        }else {
            String[] id = ids.split(",");
            for (String i: id) {
                itemMapper.deleteByPrimaryKey(Long.parseLong(i));
            }
            return E3Result.ok();
        }
    }
}
