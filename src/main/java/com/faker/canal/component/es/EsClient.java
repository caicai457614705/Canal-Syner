package com.faker.canal.component.es;

import com.faker.canal.client.ClientFactory;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.InetAddress;

/**
 * Created by faker on 18/2/8.
 */
public class EsClient {

    private final static Logger logger = LoggerFactory.getLogger(ClientFactory.class);

    private static volatile TransportClient client;

    private static String clusterName = "elasticsearch";

    private static void initClient(String[] hosts, String clusterName) {

        Assert.isTrue(hosts.length > 0);
        if (clusterName != null) {
            EsClient.clusterName = clusterName;
        }
        TransportAddress[] transportAddresses = new TransportAddress[hosts.length];
        try {

            for (int i = 0; i < hosts.length; i++) {
                String[] hostPort = hosts[i].split(":");
                transportAddresses[i] = new TransportAddress(InetAddress.getByName(hostPort[0]), Integer.valueOf(hostPort[1]));
            }
            Settings settings = Settings.builder().put("client.transport.sniff", true)
                    .put("cluster.name", clusterName).build();
            client = new PreBuiltTransportClient(settings).addTransportAddresses(transportAddresses);
        } catch (Exception e) {
            logger.error("init esclient failed ", e);
        }
    }

    public static TransportClient getClient(String[] hosts, String clusterName) {
        if (client == null) {
            synchronized (EsClient.class) {
                if (client == null) {
                    initClient(hosts, clusterName);
                }
            }
        }
        return client;
    }

    public static TransportClient getClient(String[] hosts) {
        if (client == null) {
            synchronized (EsClient.class) {
                if (client == null) {
                    initClient(hosts, null);
                }
            }
        }
        return client;
    }

    public static String getClusterName() {
        return clusterName;
    }

}
