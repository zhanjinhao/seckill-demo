package cn.addenda.seckilldemo.config;

import cn.addenda.businesseasy.lock.DistributeLockService;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @datetime 2022/12/8 21:43
 */
public class SimpleRedisLockService implements DistributeLockService {

    private StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLockService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(String lockName) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(lockName, "1", 10, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public boolean tryLock(String lockName, Long threadId) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(lockName, "1", 10, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public void unlock(String lockName) {
        stringRedisTemplate.delete(lockName);
    }

    @Override
    public void unlock(String lockName, Long threadId) {
        stringRedisTemplate.delete(lockName);
    }

    @Override
    public void forceUnlock(String lockName) {
        stringRedisTemplate.delete(lockName);
    }
}
