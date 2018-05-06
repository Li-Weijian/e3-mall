package cn.e3mall.search.service.impl;

import cn.e3.commom.pojo.SearchItem;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    /**
     * 将所有商品导入索引库
     */
    @Override
    public E3Result importAllItems() {

        //1. 从数据库中查询出所有商品
        List<SearchItem> allItem = itemMapper.getAllItem();

        try {
            //2. 写入Solr索引库中
            for (SearchItem searchItem : allItem) {
                //创建一个文档对象
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                solrServer.add(document);
            }
            //提交
            solrServer.commit();
            return E3Result.ok();

        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500,"导入索引库出现异常");
        }
    }
}
