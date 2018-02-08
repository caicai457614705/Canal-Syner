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

public class KafkaProcessor extends AbstractProcessor {

    private KafkaSender kafkaSender;

    private String topic;

    public KafkaProcessor(Properties config) {
        this.kafkaSender = KafkaSender.getKafkaSender(config);
        Assert.isTrue(config.containsKey("topic"));
        this.topic = config.getProperty("topic");
    }

    @Override
    public void processDelete(List<CanalEntry.Column> colums, String schemaName, String tableName) {
        doSend(colums, schemaName, tableName, ChangeType.DELETE);
    }

    @Override
    public void processInsert(List<CanalEntry.Column> colums, String schemaName, String tableName) {
        doSend(colums, schemaName, tableName, ChangeType.INSERT);
    }

    @Override
    public void processUpdate(List<CanalEntry.Column> colums, String schemaName, String tableName) {
        doSend(colums, schemaName, tableName, ChangeType.UPDATE);
    }

    private void doSend(List<CanalEntry.Column> colums, String schemaName, String tableName, ChangeType changeType) {
        JSONObject jsonObject = new JSONObject();
        for (CanalEntry.Column column : colums) {
            jsonObject.put(column.getName(), column.getValue());
        }
        jsonObject.put("schemaName", schemaName);
        jsonObject.put("tableName", tableName);
        jsonObject.put("eventType", changeType.getValue());
        kafkaSender.sendMessage(topic, jsonObject);
    }

}
