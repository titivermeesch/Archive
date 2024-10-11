package io.minki.gapi.store;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class KeyValueStore {
    private final JedisPool pool;

    public KeyValueStore(String url) {
        this.pool = new JedisPool(url);
    }

    public String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    public void set(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
        }
    }

    public void del(String key) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(key);
        }
    }

    public boolean exists(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }
}
