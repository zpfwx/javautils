package com.myutil.redis;

/**
 * @author pfzhao
 * @title: ICacheKey
 * @projectName myUtil
 * @description: TODO
 * @date 2023/5/30 11:20
 */
public interface ICacheKey {
    String getKey();

    int getExpirationTime();

    int getLocalCacheTime();
}
