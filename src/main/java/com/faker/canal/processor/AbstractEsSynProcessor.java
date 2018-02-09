package com.faker.canal.processor;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.faker.canal.component.es.EsAPI;
import com.faker.canal.component.es.EsClient;
import org.elasticsearch.client.transport.TransportClient;

import java.util.List;

/**
 * Created by faker on 18/2/6.
 */
public abstract class AbstractEsSynProcessor extends AbstractProcessor {


    private EsAPI esAPI;

    public AbstractEsSynProcessor(String[] hosts, String clusterName,String index) {
        TransportClient client = EsClient.getClient(hosts, clusterName);
        this.esAPI = new EsAPI(client,index);
    }

    public AbstractEsSynProcessor(String[] hosts,String index) {
        TransportClient client = EsClient.getClient(hosts);
        this.esAPI = new EsAPI(client,index);
    }

}
