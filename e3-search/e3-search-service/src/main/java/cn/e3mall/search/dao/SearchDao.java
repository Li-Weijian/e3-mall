package cn.e3mall.search.dao;

import cn.e3.commom.pojo.SearchItem;
import cn.e3.commom.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery solrQuery) throws SolrServerException {

        SearchResult searchResult = new SearchResult();
        List<SearchItem> list = new ArrayList<>();

        //根据solrQuery查询索引库
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //取到结果集
        SolrDocumentList documents = queryResponse.getResults();

        //设置总商品数
        searchResult.setRecourdCount(documents.getNumFound());
        //获取高亮
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        String title = "";

        for (SolrDocument document:documents) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId((String) document.getFieldValue("id"));
            searchItem.setCategory_name((String) document.getFieldValue("item_category_name"));
            searchItem.setImage((String) document.getFieldValue("item_image"));
            searchItem.setPrice((Long) document.getFieldValue("item_price"));
            List<String> titles = highlighting.get(document.getFieldValue("id")).get("item_title");
            //判断高亮标题是否存在
            if (titles != null && titles.size() > 0 ){
                //存在，则取出并赋值
                title = titles.get(0);
            }else{
                //不存在，取出原标题并赋值
                title = (String) document.getFieldValue("item_title");
            }
            searchItem.setTitle(title);

            //添加到list中
            list.add(searchItem);
        }
        searchResult.setItemList(list);

        return searchResult;
    }

}
