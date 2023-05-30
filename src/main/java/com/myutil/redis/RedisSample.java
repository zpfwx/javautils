package com.myutil.redis;

import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author pfzhao
 * @title: RedisSample
 * @projectName myUtil
 * @description: TODO
 * @date 2023/5/30 11:28
 */
@Log4j
public class RedisSample {
    @Resource
    private RedisManager redisManager;
    final static String INSTALL_NO_OPERATE_LOCK= "InstallNoOperateLockKey";
    @Scheduled(cron = "0 0/3 * * * ?")
    public void run() {
        ICacheKey key = new InstallStatusLockKey(INSTALL_NO_OPERATE_LOCK);
        if (!redisManager.tryGetDistributedLock(key,"1")) {
            return;
        }
        try {
            //scheduledDeviceBaseInfoService.initInstallStatus(expireTime);
            log.info("end CorrectDeviceInstallStatus...........");
        } catch (Exception e) {
            log.error("CorrectDeviceInstallStatus call no operate!",e);
        } finally {
            redisManager.del(key);
        }

    }

    /**
     * 缓存键
     */
    static class InstallStatusLockKey implements ICacheKey {

        private String lockKey;

        private InstallStatusLockKey() {
            lockKey = "INSTALL_NO_OPERATE_LOCK";
        }

        private InstallStatusLockKey(String lockKey) {
            this.lockKey = lockKey;
        }

        @Override
        public String getKey() {
            return lockKey;
        }

        @Override
        public int getExpirationTime() {
            return -1;
        }

        @Override
        public int getLocalCacheTime() {
            return 0;
        }
    }
}
