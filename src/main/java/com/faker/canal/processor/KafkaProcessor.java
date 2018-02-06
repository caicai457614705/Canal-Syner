package com.faker.canal.processor;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.faker.canal.component.kafka.KafkaProducer;

import java.util.List;

/**
 * Created by faker on 18/2/6.
 */

public class KafkaProcessor extends AbstractProcessor {

    private KafkaProducer kafkaProducer = new KafkaProducer();

    @Override
    public void processDelete(List<CanalEntry.Column> colums) {

    }

    @Override
    public void processInsert(List<CanalEntry.Column> colums) {
        kafkaProducer.sendMessage();
    }

    @Override
    public void processUpdate(List<CanalEntry.Column> colums) {

    }
}
