package cn.e3mall.service;

import cn.e3.commom.easyUIGridResult.EasyUIGirdResult;
import cn.e3mall.pojo.TbItem;

public interface ItemService {

    public TbItem getItemById(long itemId);

    public EasyUIGirdResult getItemList(int page, int rows);
}
