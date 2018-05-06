package cn.e3mall.search.service;

import cn.e3.commom.pojo.SearchResult;

public interface SearchService {

    public SearchResult search(int page,int rows, String keyword) throws Exception;
}
