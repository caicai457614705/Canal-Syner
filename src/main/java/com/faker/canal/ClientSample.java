package com.faker.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by faker on 18/2/2.
 */
public class ClientSample {

    public static void main(String[] args) {
        String host = "192.168.6.69";
        int port = 11111;
        int batchSize = 1000;
        int emptyCount = 0;
//        初始化连接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(host, port), "example", "", "");
        try {
//            连接,订阅所有表
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();

            int totalEmptyCount = 120;
            //如果为空的批次不超过限制 , 循环拉取
            while (emptyCount < totalEmptyCount) {
                Message message = connector.getWithoutAck(batchSize);  //获取batchSize 的数据
//                获取ID , 和size
                long batchId = message.getId();
                int size = message.getEntries().size();
//                如果id为-1 或者长度为0 ,则没有数据, 打印为空批次, 并sleep避免空转
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    System.out.println("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("强制中断退出");
                    }
//                    如果有数据, 就打印
                } else {
                    emptyCount = 0;
                    printEntry(message.getEntries());
                }
                connector.ack(batchId);
            }

            System.out.println("empty too many times , exit");
        } finally {
            connector.disconnect();
        }
    }


    private static void printEntry(List<CanalEntry.Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
//            EntryType为事务类型跳过
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

//            使用parseFrom 读取Entry里面的数据
            CanalEntry.RowChange rowChange = null;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("处理Entry异常, 内容为: " + entry.toString(), e);
            }

//            按格式打印
            CanalEntry.EventType eventType = rowChange.getEventType();
            System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

//
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------&gt; before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------&gt; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }

        }
    }


    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
