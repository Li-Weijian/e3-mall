package cn.e3mall.service.impl;

import cn.e3.commom.easyUIGridResult.EasyUIGirdResult;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

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
}
