package com.faker.canal.processor;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

import java.util.List;

/**
 * Created by faker on 18/2/6.
 */
public interface ColumnProcessor {

    void commonOperation(Message message, long batchId, int size);

    void processDelete(List<CanalEntry.Column> colums);

    void processInsert(List<CanalEntry.Column> colums);

    void processUpdate(List<CanalEntry.Column> colums);
}
