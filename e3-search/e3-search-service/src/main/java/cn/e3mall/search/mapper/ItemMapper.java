package cn.e3mall.search.mapper;

import cn.e3.commom.pojo.SearchItem;
import cn.e3mall.pojo.TbItem;

import java.util.List;

public interface ItemMapper {

    List<SearchItem> getAllItem();

    SearchItem getItemById(Long id);

}

