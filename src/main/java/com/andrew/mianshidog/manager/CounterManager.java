package com.andrew.mianshidog.manager;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 通用计数器（可用于反爬虫，频率统计）
 */
@Slf4j
@Service
public class CounterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 增加并返回计数，默认统计一分钟内的计数结果
     *
     * @param key
     * @return
     */
    public long incrementAndGet(String key) {
        return incrAndGetCounter(key, 1, TimeUnit.MINUTES);
    }

    /**
     * 增加并返回计数
     *
     * @param key
     * @param timeInterval
     * @param timeUnit
     * @return
     */
    public long incrAndGetCounter(String key, int timeInterval, TimeUnit timeUnit) {
        int expirationTimeInSeconds;
        switch (timeUnit) {
            case SECONDS:
                expirationTimeInSeconds = timeInterval;
                break;
            case MINUTES:
                expirationTimeInSeconds = timeInterval * 60;
                break;
            case HOURS:
                expirationTimeInSeconds = timeInterval * 60 * 60;
                break;
            default:
                throw new IllegalArgumentException("Unsupported TimeUnit. Use SECONDS, MINUTES, or HOURS.");
        }

        return incrAndGetCounter(key, timeInterval, timeUnit, expirationTimeInSeconds);
    }

    /**
     * 增加并返回计数
     *
     * @param key
     * @param timeInterval
     * @param timeUnit
     * @param expireTime
     * @return
     */
    public long incrAndGetCounter(String key, int timeInterval, TimeUnit timeUnit, long expireTime) {
        if (StrUtil.isBlank(key)) {
            return 0;
        }

        // 根据时间粒度生成 Redis key
        long timeFactor;
        long epochSeconds = Instant.now().getEpochSecond();
        switch (timeUnit) {
            case SECONDS:
                timeFactor = epochSeconds / timeInterval;
                break;
            case MINUTES:
                timeFactor = epochSeconds / 60;
                break;
            case HOURS:
                timeFactor = epochSeconds / 3600;
            default:
                throw new IllegalArgumentException("不支持的单位");
        }

        String redisKey = key + ":" + timeFactor;

        // Lua脚本
        String luaScript =
                "if redis.call('exists', KEYS[1]) == 1 then " +
                        "  return redis.call('incr', KEYS[1]); " +
                        "else " +
                        "  redis.call('set', KEYS[1], 1); " +
                        "  redis.call('expire', KEYS[1], ARGV[1]); " +
                        "  return 1; " +
                        "end";

        // 执行Lua脚本
        RScript script = redissonClient.getScript();
        Object countObj = script.eval(
                RScript.Mode.READ_WRITE,
                luaScript,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(redisKey), expireTime
        );
        return (long) countObj;
    }
}
