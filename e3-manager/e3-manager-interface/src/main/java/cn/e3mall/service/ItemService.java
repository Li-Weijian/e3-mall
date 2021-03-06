package cn.e3mall.service;

import cn.e3.commom.pojo.EasyUIGirdResult;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

    public TbItem getItemById(long itemId);

    public EasyUIGirdResult getItemList(int page, int rows);

    public E3Result addItem(TbItem item, String desc);

    public E3Result updateItem(TbItem item, String desc);

    public E3Result deleteItem(String ids);

    public TbItemDesc getItemDescById(long itemId);
}
