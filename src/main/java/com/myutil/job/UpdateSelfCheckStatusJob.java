package com.myutil.job;


import com.myutil.redis.ICacheKey;
import com.myutil.redis.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName: UpdateSelfCheckStatusJob
 * @Creator: tony
 * @Date: 2022/4/8
 * @Description: 超过30天没有自检的设备，自检状态变更为unchecked
 **/

@Component
@EnableScheduling
@Slf4j
public class UpdateSelfCheckStatusJob {

    @Resource
    RedisManager redisManager;

   // @Autowired
    //DeviceHealthCheckService deviceHealthCheckService;

    /**
     * 轮询间隔时间(每天执行一次)
     */
    @Scheduled(cron = "0 40 11 * * ?")
    public void run() {
        log.info("start UpdateSelfCheckStatusJob...........");
        ICacheKey key = new UpdateSelfCheckStatusLockKey();
        if(!redisManager.tryGetDistributedLock(key, "1")) {
            return;
        }
        try {
            //deviceHealthCheckService.updateDeviceHealthCheckStatus2UnCheck();
        } finally {
            redisManager.del(key);
        }
        log.info("end UpdateSelfCheckStatusJob...........");
    }

    /**
     * 缓存键
     */
    static class UpdateSelfCheckStatusLockKey implements ICacheKey {

        static final String UPDATE_SELF_CHECK_STATUS_LOCK_KEY = "UpdateSelfCheckStatusLockKey";

        private UpdateSelfCheckStatusLockKey() {
        }

        @Override
        public String getKey() {
            return UPDATE_SELF_CHECK_STATUS_LOCK_KEY;
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
