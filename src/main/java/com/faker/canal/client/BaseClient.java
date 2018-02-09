package com.faker.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.faker.canal.processor.AbstractProcessor;
import com.faker.canal.processor.example.DefaultProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Created by faker on 18/2/6.
 */
public class BaseClient {
    private final static Logger logger = LoggerFactory.getLogger(BaseClient.class);

    private int batchSize = 5 * 1024;
    private long timeout = 1000L;
    private volatile boolean running = false;
    private CanalConnector connector;
    private Thread thread = null;
    private String destination;
    private AbstractProcessor processor = new DefaultProcessor();
    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {

        public void uncaughtException(Thread t, Throwable e) {
            logger.error("parse events has an error", e);
        }
    };


    public BaseClient(String destination) {
        this(destination, null);
    }

    public BaseClient(String destination, CanalConnector connector) {
        this.destination = destination;
        this.connector = connector;
    }

    public void start() {
        Assert.notNull(connector, "connector is null");
        thread = new Thread(new Runnable() {

            public void run() {
                process();
            }
        });

        thread.setUncaughtExceptionHandler(handler);
        thread.start();
        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }
        MDC.remove("destination");
    }

    private void process() {
        while (running) {
            try {
                MDC.put("destination", destination);
                connector.connect();
                connector.subscribe(null);
                while (running) {
                    Message message = connector.getWithoutAck(batchSize, timeout, TimeUnit.MILLISECONDS); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        // try {
                        // Thread.sleep(1000);
                        // } catch (InterruptedException e) {
                        // }
                    } else {
                        processor.commonOperation(message, batchId, size);
                    }

                    connector.ack(batchId); // 提交确认
                    // connector.rollback(batchId); // 处理失败, 回滚数据
                }
            } catch (Exception e) {
                logger.error("process error!", e);
            } finally {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }


    public void setConnector(CanalConnector connector) {
        this.connector = connector;
    }

    public CanalConnector getConnector() {
        return connector;
    }

    public void setProcessor(AbstractProcessor processor) {
        this.processor = processor;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
