package com.faker.canal.processor.example;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.faker.canal.processor.AbstractProcessor;

import java.util.List;

/**
 * Created by faker on 18/2/6.
 */
public class DefaultProcessor extends AbstractProcessor {

    @Override
    public void processDelete(List<CanalEntry.Column> colums, String schemaName, String tableName) {

    }

    @Override
    public void processInsert(List<CanalEntry.Column> colums, String schemaName, String tableName) {

    }

    @Override
    public void processUpdate(List<CanalEntry.Column> colums, String schemaName, String tableName) {

    }
}
