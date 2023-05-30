package com.myutil.redis;


import com.alibaba.fastjson.JSON;
import lombok.Synchronized;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author pfzhao
 * @title: RedisManager
 * @projectName myUtil
 * @description: TODO
 * @date 2023/5/30 11:19
 */
public class RedisManager {
    public static final Logger logger = LoggerFactory.getLogger(RedisManager.class);
    private RedissonClient redissonClient;

    private static RedisManager INSTANCE;

    private RedisManager(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Synchronized
    public static void initRedisManager(RedissonClient redissonClient){
        INSTANCE = new RedisManager(redissonClient);
    }
    public static RedisManager getInstance() {
        if(INSTANCE == null) {
            throw new RuntimeException("RedisManager has not initialized!");
        }
        return INSTANCE;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void del(ICacheKey key) {
        this.redissonClient.getBucket(key.getKey()).delete();
    }

    public long expire(ICacheKey key) {
        if (key.getExpirationTime() > 0) {
            this.redissonClient.getBucket(key.getKey()).expire(key.getExpirationTime(), TimeUnit.SECONDS);
            return key.getExpirationTime();
        }
        return -1L;
    }

    public void put(ICacheKey key, String value) {
        if(key.getExpirationTime()>=0) {
            this.redissonClient.getBucket(key.getKey(), StringCodec.INSTANCE).set(value, key.getExpirationTime(), TimeUnit.SECONDS);
        }else{
            this.redissonClient.getBucket(key.getKey(), StringCodec.INSTANCE).set(value);
        }
    }

    public void put(ICacheKey key, Object object) {
        if(key.getExpirationTime()>=0) {
            this.redissonClient.getBucket(key.getKey()).set(object, key.getExpirationTime(), TimeUnit.SECONDS);
        }else{
            this.redissonClient.getBucket(key.getKey(), StringCodec.INSTANCE).set(object);
        }
    }

    public void put(String key, String value, Integer expirationTime) {
        if(expirationTime>=0) {
            this.redissonClient.getBucket(key, StringCodec.INSTANCE).set(value, expirationTime, TimeUnit.SECONDS);
        }else{
            this.redissonClient.getBucket(key, StringCodec.INSTANCE).set(value);
        }
    }

    public void setString(ICacheKey key, String value) {
        if(key.getExpirationTime()>=0) {
            this.redissonClient.getBucket(key.getKey(), StringCodec.INSTANCE).set(value, key.getExpirationTime(), TimeUnit.SECONDS);
        }else{
            this.redissonClient.getBucket(key.getKey(), StringCodec.INSTANCE).set(value);
        }
    }

    public void setString(String key, String value) {
        this.redissonClient.getBucket(key, StringCodec.INSTANCE).set(value);
    }

    public void setString(String key, String value, long time, TimeUnit timeUnit) {
        if(time>=0) {
            this.redissonClient.getBucket(key, StringCodec.INSTANCE).set(value, time, timeUnit);
        }else{
            this.redissonClient.getBucket(key, StringCodec.INSTANCE).set(value);
        }
    }

    public String getStr(ICacheKey key) {
        return this.redissonClient.<String>getBucket(key.getKey()).get();
    }

    public String get(ICacheKey key) {
        return this.redissonClient.<String>getBucket(key.getKey(), StringCodec.INSTANCE).get();
    }

    public boolean exists(ICacheKey key) {
        return this.redissonClient.getBucket(key.getKey()).isExists();
    }

    public boolean exists(String key) {
        return this.redissonClient.getBucket(key).isExists();
    }

    public boolean setnx(String key, String expireTime) {
        return redissonClient.getBucket(key, StringCodec.INSTANCE).trySet(expireTime);
    }

    public String get(String key) {
        return this.redissonClient.<String>getBucket(key, StringCodec.INSTANCE).get();
    }


    public void del(String key) {
        this.redissonClient.getBucket(key).delete();
    }

    public boolean deleteKey(String key) {
        return this.redissonClient.getBucket(key).delete();
    }

    public void delBatch(List<String> keyList) {
        Iterator<String> var3 = keyList.iterator();
        RBatch rBatch = this.redissonClient.createBatch();
        while (var3.hasNext()) {
            rBatch.getBucket(var3.next()).deleteAsync();
        }
        rBatch.executeAsync();
    }

    public void push(ICacheKey listKeyName, String object) {
        this.push(listKeyName, object);
    }

    public void lpush(ICacheKey listKeyName, String object) {
        redissonClient.<String>getDeque(listKeyName.getKey(), StringCodec.INSTANCE).addFirst(object);
        if(listKeyName.getExpirationTime() >= 0) {
            redissonClient.getDeque(listKeyName.getKey()).expire(listKeyName.getExpirationTime(), TimeUnit.SECONDS);
        }
    }

    public void rpush(String listKeyName, List<String> object, long expirationTime) {
        redissonClient.<String>getDeque(listKeyName, StringCodec.INSTANCE).addAll(object);
        if(expirationTime >= 0) {
            redissonClient.getDeque(listKeyName).expire(expirationTime, TimeUnit.SECONDS);
        }
    }

    public void rpush(String listKeyName, String value, long expirationTime) {
        redissonClient.<String>getDeque(listKeyName, StringCodec.INSTANCE).add(value);
        if(expirationTime >= 0) {
            redissonClient.getDeque(listKeyName).expire(expirationTime, TimeUnit.SECONDS);
        }
    }

    public List<String> lpop(String listKeyName, Integer popNum) {
        return redissonClient.<String>getDeque(listKeyName, StringCodec.INSTANCE).poll(popNum);
    }

    public String lpopFirst(String listKeyName) {
        return redissonClient.<String>getDeque(listKeyName, StringCodec.INSTANCE).pollFirst();
    }

    public void lpush(ICacheKey listKeyName, Object object) {
        redissonClient.getDeque(listKeyName.getKey(), StringCodec.INSTANCE).addFirst(JSON.toJSONString(object));
        if(listKeyName.getExpirationTime() >= 0) {
            redissonClient.getDeque(listKeyName.getKey()).expire(listKeyName.getExpirationTime(), TimeUnit.SECONDS);
        }
    }
    public void lpopAll(ICacheKey listKeyName) {
        redissonClient.<String>getDeque(listKeyName.getKey()).clear();
    }

    public String rpop(ICacheKey listKeyName) {
        return redissonClient.<String>getDeque(listKeyName.getKey(), StringCodec.INSTANCE).pollLast();
    }

    public String pop(ICacheKey listKeyName) {
        return this.pop(listKeyName, false);
    }

    public String pop(ICacheKey listKeyName, boolean blockingWhenEmpty) {
        return redissonClient.<String>getDeque(listKeyName.getKey()).pollLast();
    }

    public boolean tryGetDistributedLock(ICacheKey key, String requestId) {
        boolean ret = false;
        try {
            ret = redissonClient.getLock(key.getKey()).tryLock(0, key.getExpirationTime(), TimeUnit.SECONDS);
        }catch (Exception ex){
        }

        return ret;
    }

    public Integer ttl(ICacheKey key) {
        Long ttl = redissonClient.getBucket(key.getKey()).remainTimeToLive();

        Integer ttlInt;
        try {
            ttlInt = Integer.valueOf(String.valueOf(ttl));
        } catch (Exception var5) {
            ttlInt = null;
        }

        return ttlInt;
    }

    /**
     * <p>
     * 设置key value,如果key已经存在则返回0,nx==> not exist
     * </p>
     *
     * @param key
     * @param value
     * @return boolean
     */
    public boolean setNx(String key, String value) {
        return redissonClient.getBucket(key, StringCodec.INSTANCE).trySet(value);
    }

    /**
     * <p>
     * 设置key value,如果key已经存在则返回0,nx==> not exist
     * </p>
     *
     * @param key
     * @param value
     * @param seconds 过期时间 秒
     * @return boolean
     */
    public boolean setNx(String key,String value, int seconds) {
        if(seconds > 0){
            return redissonClient.getBucket(key, StringCodec.INSTANCE).trySet(value,seconds,TimeUnit.SECONDS);
        } else {
            return setNx(key,value);
        }
    }

    /**
     * <p>
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     * </p>
     *
     * @param key
     * @return 加值后的结果
     */
    public Long incr(String key) {
        return redissonClient.getAtomicLong(key).incrementAndGet();
    }

}
