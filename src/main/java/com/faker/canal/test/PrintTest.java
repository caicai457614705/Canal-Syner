package com.faker.canal.test;

import com.faker.canal.client.BaseClient;
import com.faker.canal.client.ClientFactory;
import com.faker.canal.enums.ModeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by faker on 18/2/6.
 */
public class PrintTest {
    private final static Logger logger = LoggerFactory.getLogger(PrintTest.class);

    public static void main(String[] args) {
        BaseClient baseClient = ClientFactory.getClient(ModeEnum.Simple, "localhost", 11111, "test");
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
