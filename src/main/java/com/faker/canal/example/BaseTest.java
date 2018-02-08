package com.faker.canal.example;

import com.faker.canal.client.BaseClient;
import com.faker.canal.client.ClientFactory;
import com.faker.canal.enums.ModeEnum;
import com.faker.canal.processor.KafkaProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by faker on 18/2/6.
 */
public class BaseTest {
    private final static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    public static void main(String[] args) {
        BaseClient baseClient = ClientFactory.getClient(ModeEnum.Simple, "localhost", 11111, "example");
        Properties config = new Properties();
        config.put("bootstrap.servers", "localhost:9092");
        //producer部分配置
        config.put("acks", "1");
        config.put("retries", "0");
        config.put("linger.ms", "1");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //topic配置
        config.put("topic", "faker");

        KafkaProcessor kafkaProcessor = new KafkaProcessor(config);
        baseClient.setProcessor(kafkaProcessor);
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
