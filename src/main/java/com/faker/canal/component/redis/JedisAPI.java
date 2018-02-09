package com.faker.canal.component.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by faker on 18/2/8.
 */
public class JedisAPI {

    private final static Logger logger = LoggerFactory.getLogger(JedisAPI.class);

    /**
     * 保存多个连接源
     */
    private static Map<String, JedisPool> poolMap = new HashMap<String, JedisPool>();

    /**
     * @Description: jedisPool 池
     * @Param: ip, port
     */
    private static JedisPool getPool(String ip, int port) {

        try {
            String key = ip + ":" + port;
            JedisPool pool = null;
            if (!poolMap.containsKey(key)) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setTestOnBorrow(true);
                pool = new JedisPool(config, ip, port);
                poolMap.put(key, pool);
            } else {
                pool = poolMap.get(key);
            }
            return pool;
        } catch (Exception e) {
            logger.error("init jedis pool failed ! ", e);
        }
        return null;
    }

    /**
     * @Description: jedisPool 池
     * @Param: ip, port
     */
    private static JedisPool getPool(String ip, int port, Properties config) {

        if (config != null) {
            try {
                String key = ip + ":" + port;
                JedisPool pool = null;
                if (!poolMap.containsKey(key)) {
                    JedisPoolConfig jedisconfig = new JedisPoolConfig();
                    if (config.containsKey("MAX_IDLE")) {
                        jedisconfig.setMaxIdle(Integer.valueOf(config.get("MAX_IDLE").toString()));
                    }
                    if (config.containsKey("MAX_TOTAL")) {
                        jedisconfig.setMaxIdle(Integer.valueOf(config.get("MAX_TOTAL").toString()));
                    }
                    //  在获取连接的时候检查有效性, 默认false
                    jedisconfig.setTestOnBorrow(true);

                    pool = new JedisPool(jedisconfig, ip, port);
                    poolMap.put(key, pool);
                } else {
                    pool = poolMap.get(key);
                }
                return pool;
            } catch (Exception e) {
                logger.error("init jedis pool failed ! ", e);
            }
        } else {
            getPool(ip, port);
        }

        return null;
    }


    /**
     * @Description: 获取一个jedis连接
     * @Param: ip, port
     * @return: redis.clients.jedis.Jedis
     */
    public static Jedis getRedis(String ip, int port) {
        Jedis jedis = null;
        try {
            JedisPool pool = getPool(ip, port);
            if (pool != null) {
                jedis = pool.getResource();
            }
        } catch (Exception e) {
            logger.error("get redis failed ! ", e);
        }
        return jedis;
    }

    /**
     * @Description: 获取一个jedis连接
     * @Param: ip, port
     * @return: redis.clients.jedis.Jedis
     */
    public static Jedis getRedis(String ip, int port, Properties config) {
        Jedis jedis = null;
        try {
            JedisPool pool = getPool(ip, port, config);
            if (pool != null) {
                jedis = pool.getResource();
            }
        } catch (Exception e) {
            logger.error("get redis failed ! ", e);
        }
        return jedis;
    }

    /**
     * @Description: 释放jedis到jedisPool中
     * @Param: jedis, ip, port
     * @return: void
     */
    public void closeRedis(Jedis jedis) {

        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e) {
                logger.error("colse jedis failed ! ", e);
            }
        }
    }
}
