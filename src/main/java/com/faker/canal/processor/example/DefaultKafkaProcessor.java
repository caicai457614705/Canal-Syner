package com.faker.canal.processor.example;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.faker.canal.enums.ChangeType;
import com.faker.canal.processor.AbstractKafkaProcessor;

import java.util.List;
import java.util.Properties;

/**
 * Created by faker on 18/2/9.
 */
public class DefaultKafkaProcessor extends AbstractKafkaProcessor {

    public DefaultKafkaProcessor(Properties config) {
        super(config);
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
