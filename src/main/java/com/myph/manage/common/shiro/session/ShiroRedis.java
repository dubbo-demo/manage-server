package com.myph.manage.common.shiro.session;

import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class ShiroRedis {
    public ShiroRedis() {

    }

    public static final String NAMESPACE_SEPARATOR = ":";


    private Long expire = -1l;
    private String sessionRediskeyPrefix;
    private String cacheRediskeyPrefix;
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除 缓存
     *
     * @param sessionId
     * @return
     * @throws Exception
     */
    public String deleteCached(final byte[] sessionId) throws Exception {
        redisTemplate.execute(new RedisCallback<Object>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.del(sessionId);
                return null;
            }
        });
        return null;
    }

    /**
     * 更新 缓存
     *
     * @param key
     * @param session
     * @param expireSec
     * @return
     * @throws Exception
     */
    public String updateCached(final byte[] key, final byte[] session, final Long expireSec)
            throws Exception {
        return (String) redisTemplate.execute(new RedisCallback<Object>() {
            public String doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                connection.set(key, session);
                if (expireSec != null) {
                    connection.expire(key, expireSec/1000);
                } else {
                    connection.expire(key, expire/1000);
                }
                return new String(key);
            }
        });

    }

    /**
     * 获取缓存
     *
     * @param sessionId
     * @return
     * @throws Exception
     */
    public Object getCached(final byte[] sessionId) throws Exception {
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] bs = connection.get(sessionId);
                return SerializeUtils.deserialize(bs);
            }
        });

    }

    /**
     * 根据 正则表达式key 获取 列表
     *
     * @param keys
     * @return
     * @throws Exception
     */
    public Set getKeys(final byte[] keys) throws Exception {
        return redisTemplate.execute(new RedisCallback<Set>() {
            public Set doInRedis(RedisConnection connection)
                    throws DataAccessException {
                Set<byte[]> setByte = connection.keys(keys);
                if (setByte == null || setByte.size() < 1) {
                    return null;
                }
                Set set = new HashSet();
                for (byte[] key : setByte) {
                   // byte[] bs = connection.get(key);
                    set.add(key);
                }

                return set;

            }
        });
    }

    /**
     * 根据 正则表达式key 获取 列表
     *
     * @param keys
     * @return
     * @throws Exception
     */
    public Set getValues(final byte[] keys) throws Exception {
        return redisTemplate.execute(new RedisCallback<Set>() {
            public Set doInRedis(RedisConnection connection)
                    throws DataAccessException {
                Set<byte[]> setByte = connection.keys(keys);
                if (setByte == null || setByte.size() < 1) {
                    return null;
                }
                Set set = new HashSet();
                for (byte[] key : setByte) {
                    byte[] bs = connection.get(key);
                    set.add(SerializeUtils.deserialize(bs));
                }

                return set;

            }
        });
    }


    public Long getDBSize() throws Exception {
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                Long len = connection.dbSize();

                return len;

            }
        });
    }

    public void clearDB() throws Exception {
        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.flushDb();
                return null;

            }
        });
    }


    public static String getNamespaceSeparator() {
        return NAMESPACE_SEPARATOR;
    }

    public String getSessionRediskeyPrefix() {
        return sessionRediskeyPrefix;
    }

    public void setSessionRediskeyPrefix(String sessionRediskeyPrefix) {
        this.sessionRediskeyPrefix = sessionRediskeyPrefix;
    }

    public String getCacheRediskeyPrefix() {
        return cacheRediskeyPrefix;
    }

    public void setCacheRediskeyPrefix(String cacheRediskeyPrefix) {
        this.cacheRediskeyPrefix = cacheRediskeyPrefix;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
