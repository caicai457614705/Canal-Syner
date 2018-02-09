package com.faker.canal.processor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.faker.canal.component.kafka.KafkaSender;
import com.faker.canal.enums.ChangeType;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Properties;

/**
 * Created by faker on 18/2/6.
 */

public abstract class AbstractKafkaProcessor extends AbstractProcessor {

    protected KafkaSender kafkaSender;

    protected String topic;

    public AbstractKafkaProcessor(Properties config) {
        this.kafkaSender = KafkaSender.getKafkaSender(config);
        Assert.isTrue(config.containsKey("topic"));
        this.topic = config.getProperty("topic");
    }


}
