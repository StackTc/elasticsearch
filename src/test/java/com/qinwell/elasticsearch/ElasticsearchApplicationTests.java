package com.qinwell.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ElasticsearchApplicationTests {

	@Test
	public void contextLoads() {
		/**
		 * testa
		 */
		System.out.println("test");
		log.info("test");
	}

	@Test
	public void searchTest() throws UnknownHostException {
		//选择es中的集群,集群默认elasticsearch
		Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();
		//创建访问客户端,需要知道具体节点的ip跟端口号
		InetAddress inetAddress = InetAddress.getByName("39.104.207.251");
		//端口用9300 ，
		// 9300端口是使用tcp客户端连接使用的端口；
		//9200端口是通过http协议连接es使用的端口；
		TransportAddress transportAddress = new TransportAddress(inetAddress,9300);
		TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
		//数据查询
		GetResponse getResponse = client.prepareGet("stacktc","test","1").execute().actionGet();
		System.out.println("data:" + getResponse.getSourceAsString());
		log.info("data:{}" , getResponse.getSourceAsString());
		client.close();
	}

}

