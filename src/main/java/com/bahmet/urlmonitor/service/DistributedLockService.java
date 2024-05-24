package com.bahmet.urlmonitor.service;

import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistributedLockService {

    private final StringRedisTemplate stringRedisTemplate;
    @Value("${urlmonitor.distributed-lock.lock-key-prefix}")
    private String LOCK_KEY_PREFIX;

    public Set<String> getLockedEndpointsKeys() {
        return stringRedisTemplate.keys(LOCK_KEY_PREFIX + "*");
    }


    public List<Long> parseLockedEndpointIds(Set<String> lockedUrlKeys) {
        return lockedUrlKeys.stream()
                .map(key -> Long.parseLong(key.replace(LOCK_KEY_PREFIX, "")))
                .toList();
    }


    public boolean lockEndpoint(MonitoredEndpoint endpoint) {
        log.info("Attempting to acquire lock for endpoint: {}", endpoint.getId());

        String lockKey = LOCK_KEY_PREFIX + endpoint.getId();

        Boolean lockAcquired = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 2, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(lockAcquired)) {
            log.info("Lock acquired for endpoint: {}", endpoint.getId());

            return true;
        }

        log.warn("Failed to acquire lock for endpoint: {}", endpoint.getId());

        return false;
    }


    public void releaseLock(MonitoredEndpoint endpoint) {
        String lockKey = LOCK_KEY_PREFIX + endpoint.getId();

        stringRedisTemplate.delete(lockKey);

        log.info("Released lock for endpoint: {}", endpoint.getId());
    }
}
