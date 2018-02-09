package com.faker.canal.processor;

import com.faker.canal.component.redis.JedisAPI;
import redis.clients.jedis.Jedis;

import java.util.Properties;

/**
 * Created by faker on 18/2/6.
 */
public abstract class AbstractRedisSynProcessor extends AbstractProcessor {

    protected Jedis jedis;

    public AbstractRedisSynProcessor(String ip, int port) {
        this.jedis = JedisAPI.getRedis(ip, port);
    }

    public AbstractRedisSynProcessor(String ip, int port, Properties config) {
        this.jedis = JedisAPI.getRedis(ip, port, config);
    }

}
