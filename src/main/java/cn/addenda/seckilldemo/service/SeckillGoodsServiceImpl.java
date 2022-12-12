package cn.addenda.seckilldemo.service;

import cn.addenda.businesseasy.util.BEDateUtils;
import cn.addenda.me.lockedselect.LockedSelectHelper;
import cn.addenda.se.lock.LockAttribute;
import cn.addenda.se.lock.LockUtils;
import cn.addenda.se.lock.Locked;
import cn.addenda.se.result.ServiceException;
import cn.addenda.se.result.ServiceResult;
import cn.addenda.se.transaction.TransactionUtils;
import cn.addenda.seckilldemo.manager.GoodsManager;
import cn.addenda.seckilldemo.manager.SeckillGoodsManager;
import cn.addenda.seckilldemo.manager.SeckillOrderManager;
import cn.addenda.seckilldemo.po.Goods;
import cn.addenda.seckilldemo.po.SeckillGoods;
import cn.addenda.seckilldemo.po.SeckillOrder;
import cn.addenda.seckilldemo.util.UserHolder;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static cn.addenda.seckilldemo.constant.RedisConstant.*;

/**
 * @author addenda
 * @datetime 2022/12/10 10:03
 */
@Component
@Slf4j
public class SeckillGoodsServiceImpl implements SeckillGoodsService, InitializingBean {

    @Autowired
    private GoodsManager goodsManager;

    @Autowired
    private SeckillGoodsManager seckillGoodsManager;

    @Autowired
    private SeckillOrderManager seckillOrderManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    private static final LinkedBlockingQueue<Pair<Long, String>> queue = new LinkedBlockingQueue<>();

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("redis/seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Locked(prefix = "seckillGoods", keyExtractor = "#this.goodsId")
    public ServiceResult<SeckillGoods> createSeckillGoods(SeckillGoods seckillGoods) {
        // 要求数据实时性，不能走缓存
        Goods goods = goodsManager.queryByIdWithDB(seckillGoods.getGoodsId());
        if (goods == null) {
            throw new ServiceException("商品不存在！");
        }

        if (seckillGoods.getStock() > goods.getStock()) {
            throw new ServiceException("秒杀商品库存最多设置为：" + goods.getStock() + "。");
        }

        LocalDateTime startDatetime = seckillGoods.getStartDatetime();
        LocalDateTime endDatetime = seckillGoods.getEndDatetime();
        if (seckillGoodsManager.datetimeConflict(seckillGoods.getGoodsId(), startDatetime, endDatetime)) {
            throw new ServiceException("[" +
                    BEDateUtils.format(startDatetime, "yyyy-MM-dd HH:mm:ss") + ", " +
                    BEDateUtils.format(endDatetime, "yyyy-MM-dd HH:mm:ss") + "]" +
                    "时间范围内已经存在该商品的秒杀活动！");
        }

        seckillGoodsManager.insert(seckillGoods);
        stringRedisTemplate.opsForValue().set(SECKILL_GOODS_STOCK_KEY + seckillGoods.getId(), String.valueOf(seckillGoods.getStock()));
        return ServiceResult.success(seckillGoods);
    }

    @Override
    public ServiceResult<SeckillGoods> queryLatestSeckill(Long goodsId) {
        return ServiceResult.success(seckillGoodsManager.queryLatestSeckill(goodsId));
    }

    private void assertSeckillActive(Long id) {
        SeckillGoods seckillGoods = seckillGoodsManager.queryById(id);
        if (seckillGoods == null) {
            throw new ServiceException("秒杀商品不存在！");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDatetime = seckillGoods.getStartDatetime();
        LocalDateTime endDatetime = seckillGoods.getEndDatetime();
        // 每个秒杀时间段都是左闭右开
        if (now.isBefore(startDatetime)) {
            throw new ServiceException("秒杀未开始！");
        } else if (now.isAfter(endDatetime) || now.equals(endDatetime)) {
            throw new ServiceException("秒杀已结束！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Boolean> seckillWithPessimismLock(Long id) {
        assertSeckillActive(id);
        String userId = UserHolder.getUserId();

        return doSeckillWithPessimismLock(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Boolean> seckillWithPessimismLockAndOnePersonOneOrder(Long id) {
        assertSeckillActive(id);
        String userId = UserHolder.getUserId();

        LockAttribute attribute = LockAttribute.LockAttributeBuilder.newBuilder().build();
        attribute.setLockFailedMsg("系统繁忙，请重试！");
        return LockUtils.doLock(attribute, () -> {
            if (seckillOrderManager.exists(id, userId)) {
                throw new ServiceException("一人只能购买一单！");
            }
            return doSeckillWithPessimismLock(id, userId);
        }, SECKILL_WITH_PESSIMISM_LOCK_AND_ONE_PERSON_ONE_ORDER_KEY + id + ":" + userId);
    }

    private ServiceResult<Boolean> doSeckillWithPessimismLock(Long id, String userId) {
        Long stock = LockedSelectHelper.wSelect(() -> seckillGoodsManager.queryStock(id));
        if (stock <= 0) {
            return ServiceResult.success(false);
        }
        if (!seckillGoodsManager.seckillWithPessimismLock(id)) {
            return ServiceResult.success(false);
        }
        seckillOrderManager.insert(new SeckillOrder(userId, id));
        return ServiceResult.success(true);
    }


    @Override
    public ServiceResult<Boolean> seckillWithOptimisticLock(Long id) {
        assertSeckillActive(id);
        String userId = UserHolder.getUserId();

        return doSeckillWithOptimisticLock(id, userId);
    }

    @Override
    public ServiceResult<Boolean> seckillWithOptimisticLockAndOnePersonOneOrder(Long id) {
        assertSeckillActive(id);
        String userId = UserHolder.getUserId();

        LockAttribute attribute = LockAttribute.LockAttributeBuilder.newBuilder().build();
        attribute.setLockFailedMsg("系统繁忙，请重试！");
        return LockUtils.doLock(attribute, () -> {
            if (seckillOrderManager.exists(id, userId)) {
                throw new ServiceException("一人只能购买一单！");
            }
            return doSeckillWithOptimisticLock(id, userId);
        }, SECKILL_WITH_OPTIMISTIC_LOCK_AND_ONE_PERSON_ONE_ORDER_KEY + id + ":" + userId);
    }

    private ServiceResult<Boolean> doSeckillWithOptimisticLock(Long id, String userId) {
        for (int i = 0; i < 3; i++) {
            Long stock = seckillGoodsManager.queryStock(id);
            if (stock <= 0) {
                return ServiceResult.success(false);
            }

            boolean tryOnce = TransactionUtils.doTransaction(() -> {
                if (!seckillGoodsManager.seckillWithOptimisticLock(id, stock)) {
                    return false;
                }
                seckillOrderManager.insert(new SeckillOrder(userId, id));
                return true;
            });

            if (tryOnce) {
                log.info("{} 第 {} 次尝试成功！", userId, i + 1);
                return ServiceResult.success(true);
            }
        }

        log.info("{} 尝试了 {} 次，均失败！", userId, 3);
        return ServiceResult.success(false);
    }

    @Override
    public ServiceResult<Boolean> seckillWithRedisLua(Long id) {
        assertSeckillActive(id);
        String userId = UserHolder.getUserId();

        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                SECKILL_GOODS_STOCK_KEY + id.toString(), userId, SECKILL_GOODS_ORDER_KEY + id.toString());
        int r = result.intValue();
        if (r == 1) {
            throw new ServiceException("一人只能购买一单！");
        } else if (r == 2) {
            return ServiceResult.success(false);
        }

        return ServiceResult.success(queue.offer(new Pair<>(id, userId)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Boolean> resetSeckill(String ids, Integer stock) {
        for (String id : ids.split(",")) {
            seckillOrderManager.deleteBySeckillGoodsId(Long.valueOf(id));
            seckillGoodsManager.setStock(Long.valueOf(id), stock);
            stringRedisTemplate.opsForValue().set(SECKILL_GOODS_STOCK_KEY + id, String.valueOf(stock));
            stringRedisTemplate.delete(SECKILL_GOODS_ORDER_KEY + id);
        }

        return ServiceResult.success(true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SECKILL_ORDER_EXECUTOR.submit(new SeckillOrderHandler(seckillGoodsManager, seckillOrderManager));
    }

    @Slf4j
    private static class SeckillOrderHandler implements Runnable {

        private final SeckillGoodsManager seckillGoodsManager;

        private final SeckillOrderManager seckillOrderManager;

        public SeckillOrderHandler(SeckillGoodsManager seckillGoodsManager, SeckillOrderManager seckillOrderManager) {
            this.seckillGoodsManager = seckillGoodsManager;
            this.seckillOrderManager = seckillOrderManager;
        }

        @Override
        public void run() {
            while (true) {
                Pair<Long, String> poll;
                try {
                    poll = queue.poll(100, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.error("等待阻塞队列数据时被唤醒！");
                    continue;
                }
                if (poll != null) {
                    Pair<Long, String> finalPoll = poll;
                    TransactionUtils.doTransaction(() -> {
                        seckillGoodsManager.seckillWithPessimismLock(finalPoll.getKey());
                        seckillOrderManager.insert(new SeckillOrder(finalPoll.getValue(), finalPoll.getKey()));
                    });
                }
            }
        }
    }

}
