package com.faker.canal.processor;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.List;

/**
 * Created by faker on 18/2/6.
 */
public class BaseProcessor extends AbstractProcessor {

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
