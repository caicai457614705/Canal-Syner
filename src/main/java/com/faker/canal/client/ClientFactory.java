package com.faker.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.faker.canal.enums.ModeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by faker on 18/2/6.
 */
public class ClientFactory {
    private final static Logger logger = LoggerFactory.getLogger(ClientFactory.class);

    public static BaseClient getClient(ModeEnum mode, String ip, int port, String destination) {
        CanalConnector connector = null;
        if (mode == ModeEnum.Simple) {
            connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, port),
                    destination,
                    "",
                    "");
        } else if (mode == ModeEnum.Cluster) {
            connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, port),
                    destination,
                    "",
                    "");
        } else {
            logger.error("client not support this mode {}", mode);
        }
        BaseClient baseClient = new BaseClient(destination);
        baseClient.setConnector(connector);
        return baseClient;
    }


}
