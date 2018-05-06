package cn.e3mall.search.service.impl;

import cn.e3.commom.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(int page, int rows, String keyword) throws Exception {

        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.setQuery(keyword);
        //设置分页条件
        if (page <= 0) page = 1;
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);
        //设置默认搜索域
        solrQuery.set("df","item_title");
        //设置高亮显示
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre(" <em style:\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");

        //执行查询
        SearchResult result = searchDao.search(solrQuery);

        //计算总页数
        int totalPage = (int) (result.getRecourdCount()/rows);
        if (result.getRecourdCount() % rows > 0){
            totalPage ++;
        }
        result.setTotalPages(totalPage);
        return result;
    }
}
