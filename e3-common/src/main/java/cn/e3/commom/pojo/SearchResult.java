package cn.e3.commom.pojo;

import java.io.Serializable;
import java.util.List;


/**
 * 搜索商品结果集
 * */
public class SearchResult implements Serializable {

    private List<SearchItem> itemList;
    private Long recourdCount;  //总商品个数
    private int totalPages;     //总页数

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }

    public Long getRecourdCount() {
        return recourdCount;
    }

    public void setRecourdCount(Long recourdCount) {
        this.recourdCount = recourdCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
