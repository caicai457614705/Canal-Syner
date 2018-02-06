package com.faker.canal.example;

import com.faker.canal.client.BaseClient;
import com.faker.canal.client.ClientFactory;
import com.faker.canal.enums.ModeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by faker on 18/2/6.
 */
public class BaseTest {
    protected final static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    public static void main(String[] args) {
        BaseClient baseClient = ClientFactory.getClient(ModeEnum.Simple, "192.168.6.69", 11111, "example");
        baseClient.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("## stop the canal client");
                baseClient.stop();
            } catch (Throwable e) {
                logger.warn("##something goes wrong when stopping canal:", e);
            } finally {
                baseClient.getConnector().disconnect();
                logger.info("## canal client is down.");
            }

        }));
    }
}
