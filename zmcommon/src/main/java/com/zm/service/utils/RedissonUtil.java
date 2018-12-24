package com.zm.service.utils;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class RedissonUtil {
    
	@Autowired
    private RedissonClient redissonClient;

    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    public RLock lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }
    
    public RLock lock(String lockKey, TimeUnit unit ,int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }
    
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }
    
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
    
    public void unlock(RLock lock) {
        lock.unlock();
    }

//    public void setRedissonClient(RedissonClient redissonClient) {
//        this.redissonClient = redissonClient;
//    }
    
    public void remove(final String key) {
    	RBucket<Object> ret = this.redissonClient.getBucket(key);
    	ret.delete();
    }
    
    public Object get(final String key) {
    	RBucket<Object> ret = this.redissonClient.getBucket(key);
    	return ret.get();
    }
    
    public boolean set(String key, Object value){
    	RBucket<Object> ret = this.redissonClient.getBucket(key);
    	ret.set(value);
    	return true;
    }
    
    public boolean exists(final String key) {
    	RBucket<Object> ret = this.redissonClient.getBucket(key);
    	return ret.isExists();
    }
    
    public boolean set(final String key, Object value, Long expireTime) {
    	RBucket<Object> ret = this.redissonClient.getBucket(key);
    	ret.set(value, expireTime, TimeUnit.MILLISECONDS);
    	return true;
    }
}
