package cn.e3mall.search.service;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class TestSolr {

    @Test
    public void testAddItem() throws IOException, SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");

        //创建一个文档对象SolrInputDocument
        SolrInputDocument inputDocument = new SolrInputDocument();
        //向文档对象中添加域（域必须在scheame.xml中定义）
        inputDocument.addField("id","test");
        inputDocument.addField("item_title","测试一下咯");
        inputDocument.addField("item_price",10000);
        //把文档添加到索引库
        solrServer.add(inputDocument);
        //提交
        solrServer.commit();
    }
}
