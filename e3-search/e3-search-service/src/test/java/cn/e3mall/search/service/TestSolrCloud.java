package cn.e3mall.search.service;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class TestSolrCloud {

    @Test
    public void testSolrCloud_add() throws IOException, SolrServerException {

        //创建一个Solr集群对象
        CloudSolrServer solrServer = new CloudSolrServer("192.168.25.128:2182,192.168.25.128:2183,192.168.25.128:2184");
        solrServer.setDefaultCollection("collection2");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","test");
        document.addField("item_title","hello SolrCloud");
        solrServer.add(document );
        solrServer.commit();

    }


}
