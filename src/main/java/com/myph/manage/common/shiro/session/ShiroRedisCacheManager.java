package com.myph.manage.common.shiro.session;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 *
 * INFO: Shiro 实现的缓存
 * User: zhaokai
 * Date: 2016/8/25- 10:54
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class ShiroRedisCacheManager extends AbstractCacheManager {

    private ShiroRedis shiroRedis;

    @Override
    protected Cache createCache(String cacheName) throws CacheException {
        return new ShiroRedisCache<String, Object>(cacheName,shiroRedis.getCacheRediskeyPrefix(), shiroRedis);
    }

    public ShiroRedis getShiroRedis() {
        return shiroRedis;
    }

    public void setShiroRedis(ShiroRedis shiroRedis) {
        this.shiroRedis = shiroRedis;
    }
}
