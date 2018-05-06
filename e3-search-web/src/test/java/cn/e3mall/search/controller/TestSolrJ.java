package cn.e3mall.search.controller;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

public class TestSolrJ {



    //简单查询
    @Test
    public void testSimpleSearch() throws SolrServerException {

        SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("id:test");

        QueryResponse query = solrServer.query(solrQuery);
        SolrDocumentList results = query.getResults();
        System.out.println(results.get(0).getFieldValues("item_title"));

    }


}
