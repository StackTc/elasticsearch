package com.qinwell.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ElasticsearchApplicationTests {

    private TransportClient client;

    @Before
    public void load() throws UnknownHostException {
        //选择es中的集群,集群默认elasticsearch
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        //创建访问客户端,需要知道具体节点的ip跟端口号
        InetAddress inetAddress = InetAddress.getByName("39.104.207.251");
        //端口用9300 ，
        // 9300端口是使用tcp客户端连接使用的端口；
        //9200端口是通过http协议连接es使用的端口；
        TransportAddress transportAddress = new TransportAddress(inetAddress, 9300);
        this.client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
    }

    @Test
    public void contextLoads() {
        /**
         * testa
         */
        System.out.println("test");
        log.info("test");
    }

    @Test
    public void search() throws UnknownHostException {
        //数据查询
        GetResponse getResponse = client.prepareGet("stacktc", "test", "1").execute().actionGet();
        System.out.println("data:" + getResponse.getSourceAsString());
        log.info("data:{}", getResponse.getSourceAsString());
        client.close();
    }

    @Test
    public void addDoc() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "lintaincheng");
        jsonObject.put("age", "29");
        XContentBuilder doc = XContentFactory.jsonBuilder().startObject().
                field("name", "lintiancheng")
                .field("age", 20).endObject();

        IndexResponse indexResponse = client.prepareIndex("stacktc", "test", "2").setSource(doc).get();
        log.info("文档添加结果：{}", indexResponse.status().name());
    }


    @Test
    public void deleteDoc() {
        DeleteResponse deleteResponse = client.prepareDelete("stacktc", "test", "2").get();
        log.info("文档删除结果：{}", deleteResponse.status().name());
    }


    /**
     * 更新，只有已存在文档，才可以更新。
     *
     * @throws Exception
     */
    @Test
    public void updateDoc() throws Exception {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("stacktc")
                .type("test")
                .id("1")
                .doc(
                        XContentFactory.jsonBuilder().startObject()
                                .field("name", "change")
                                .field("age", 30)
                                .endObject()
                );

        UpdateResponse updateResponse = client.update(updateRequest).get();
        log.info("更新结果：{}", updateResponse.status().name());

    }

    /**
     * 修改文档，不存在则新增
     */
    @Test
    public void upsertDoc() throws Exception {
        IndexRequest indexRequest = new IndexRequest("stacktc", "test", "3").source(
                XContentFactory.jsonBuilder().startObject()
                        .field("id", "3")
                        .field("name", "tc")
                        .field("age", 22).endObject()

        );

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("stacktc")
                .type("test").id("3").doc(
                XContentFactory.jsonBuilder().startObject()
                        .field("name", "change")
                        .field("age", 30)
                        .endObject()
        ).upsert(indexRequest);

        UpdateResponse updateResponse = client.update(updateRequest).get();
        log.info("修改结果：{}", updateResponse.status().name());
    }


    /**
     * 查询集合然后删除
     */
    @Test
    public void selectAndDelete() {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("name", "change"))
                //指定索引
                .source("stacktc")
                .get();

        long deleteCount = response.getDeleted();
        log.info("删除个数：{}", deleteCount);
    }

    /**
     * 查询所有
     */
    @Test
    public void searchAll() {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchResponse searchResponse = client.prepareSearch("stacktc")
                .setQuery(queryBuilder)
                .setSize(3).get();

        //获取结果集
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits) {
            //转换成map逐个打印
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                log.info("key:{},value:{}", key, map.get(key));
            }
        }
    }

    /**
     * 按照条件批量查询
     */
    @Test
    public void mutchQuery() {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "stacktc");
        SearchResponse searchResponse = client.prepareSearch("stacktc").setQuery(queryBuilder)
                .setSize(3).get();
        //获取结果集
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits) {
            //转换成map逐个打印
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                log.info("key:{},value:{}", key, map.get(key));
            }
        }
    }

    @Test
    public void termQuery() {
        //term 跟 term查询 都可以在这里实现，只要换termsQuery就可以
        //这里可以用多种query查询，比如rangeQuery、prefixQuery等等
        QueryBuilder queryBuilder = QueryBuilders.termQuery("name","stacktc");
        SearchResponse searchResponse = client.prepareSearch("stacktc").setQuery(queryBuilder).get();
        SearchHits searchHits = searchResponse.getHits();

        for (SearchHit searchHit : searchHits) {
            Map<String,Object> map = searchHit.getSourceAsMap();
            for (String key : map.keySet()) {
                log.info("key:{},value:{}",key,map.get(key));
            }
        }
    }

    @Test
    public void juheQuery() {
        //这里可以用各种聚合函数，比如avg、max、min等等，道理相同
        AggregationBuilder aggregationBuilder = AggregationBuilders.max("aggMax").field("age");
        SearchResponse searchResponse = client.prepareSearch("stacktc").addAggregation(aggregationBuilder).get();

        Max max = searchResponse.getAggregations().get("aggMax");
        log.info("max:{}",max.getValue());
    }


    @Test
    public void clusterQuery() {
        ClusterHealthResponse clusterHealthResponse = client.admin().cluster().prepareHealth().get();
        String clusterName = clusterHealthResponse.getClusterName();
        log.info("clusterName:{}", clusterName);

        int numOfData = clusterHealthResponse.getNumberOfDataNodes();
        log.info("numOfDate:{}", numOfData);

        for (ClusterIndexHealth indexHealth : clusterHealthResponse.getIndices().values()) {
            log.info("indexHealth");
            log.info("indexName:{},shards:{},replicas:{}", indexHealth.getIndex(),indexHealth.getNumberOfShards(),indexHealth.getNumberOfReplicas());
            ClusterHealthStatus healthStatus = indexHealth.getStatus();
            log.info("healthStatus:{}", healthStatus.toString());
        }
    }
}

