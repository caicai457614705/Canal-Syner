package com.faker.canal.component.kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by faker on 18/2/6.
 */
public class KafkaSender {

    private final static Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    private static volatile KafkaSender kafkasender;

    private KafkaSender() {

    }

    private KafkaProducer<String, String> kafkaProducer;

    public static KafkaSender getKafkaSender(Properties config) {
        if (kafkasender == null) {
            synchronized (KafkaSender.class) {
                if (kafkasender == null) {
                    kafkasender = new KafkaSender();
                    kafkasender.kafkaProducer = new KafkaProducer<String, String>(config);
                }
            }
        }
        return kafkasender;
    }

    public void sendMessage(String topic, Integer partition, String key, String value) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, partition, key, value);
        this.doSend(producerRecord);
    }

    public void sendMessage(String topic, String key, String value) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
        this.doSend(producerRecord);
    }

    public void sendMessage(String topic, String value) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, value);
        this.doSend(producerRecord);
    }

    private void doSend(ProducerRecord<String, String> producerRecord) {
        Future<RecordMetadata> future =  this.kafkaProducer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    logger.error("send kafka message error,topic : {} , partition : {} , offset : {}, msg content : {}", metadata.topic(), metadata.partition(), metadata.offset(), JSONObject.toJSONString(producerRecord), exception);
                } else {
                    logger.debug("send to kafka success , topic : {} , partition : {} , offset : {}", metadata.topic(), metadata.partition(), metadata.offset());
                }
            }
        });

    }

}
