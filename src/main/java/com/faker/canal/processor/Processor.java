package com.faker.canal.processor;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

import java.util.List;

/**
 * Created by faker on 18/2/6.
 */
public interface Processor {

    void commonOperation(Message message, long batchId, int size);

    void processDelete(List<CanalEntry.Column> colums, String schemaName, String tableName);

    void processInsert(List<CanalEntry.Column> colums, String schemaName, String tableName);

    void processUpdate(List<CanalEntry.Column> colums, String schemaName, String tableName);
}
