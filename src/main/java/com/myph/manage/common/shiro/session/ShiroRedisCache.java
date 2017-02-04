package com.myph.manage.common.shiro.session;

import com.myph.common.log.MyphLogger;
import com.myph.manage.common.shiro.SerializeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.Collection;
import java.util.Set;

/**
 * INFO: Shiro 实现的缓存
 * User: zhaokai
 * Date: 2016/8/25- 10:54
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */

public class ShiroRedisCache<K, V> implements Cache<K, V> {
    private String cacheName;
    private String name;
    private ShiroRedis shiroRedis;

    public ShiroRedisCache(String cacheName, String keyName, ShiroRedis cached) {
        this.name = keyName;
        this.cacheName = cacheName;
        this.shiroRedis = cached;
    }

    /**
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        String preKey = name
                + ShiroRedis.NAMESPACE_SEPARATOR
                + cacheName
                + (StringUtils.endsWith(cacheName, ":") ? "" : ShiroRedis.NAMESPACE_SEPARATOR)
                + key;
        return preKey.getBytes();
    }

    /**
     * 获得byte[]型的key
     *
     * @param key
     * @return
     */
    /*private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = key.toString();
            return preKey.getBytes();
        } else {
            return SerializeUtil.serialize(key);
        }
    }*/
    @Override
    public V get(K key) throws CacheException {
        try {
            if (key == null) {
                return null;
            } else {
                V value = (V) shiroRedis.getCached(getByteKey(key));
                return value;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }

    }

    @Override
    public V put(K key, V value) throws CacheException {
        try {
            shiroRedis.updateCached(getByteKey(key), SerializeUtils.serialize(value), null);
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        try {
            V previous = get(key);
            shiroRedis.deleteCached(getByteKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            shiroRedis.clearDB();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            Long longSize = shiroRedis.getDBSize();
            return longSize.intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        try {
            return shiroRedis.getKeys(getByteKey((K) "*"));
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Collection<V> values = shiroRedis.getValues(getByteKey((K) "*"));
            return values;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShiroRedis getShiroRedis() {
        return shiroRedis;
    }

    public void setShiroRedis(ShiroRedis shiroRedis) {
        this.shiroRedis = shiroRedis;
    }
}
