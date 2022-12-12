package cn.addenda.seckilldemo.manager;

import cn.addenda.businesseasy.cache.CacheHelper;
import cn.addenda.seckilldemo.constant.RedisConstant;
import cn.addenda.seckilldemo.mapper.SeckillGoodsMapper;
import cn.addenda.seckilldemo.po.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author addenda
 * @datetime 2022/12/10 11:00
 */
@Component
public class SeckillGoodsManagerImpl implements SeckillGoodsManager {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private CacheHelper cacheHelper;

    @Override
    public boolean datetimeConflict(Long goodsId, LocalDateTime startDatetime, LocalDateTime endDatetime) {
        Integer count = seckillGoodsMapper.datetimeConflict(goodsId, startDatetime, endDatetime);
        return count != null && count > 0;
    }

    @Override
    public void insert(SeckillGoods seckillGoods) {
        seckillGoodsMapper.insert(seckillGoods);
    }

    @Override
    public SeckillGoods queryLatestSeckill(Long goodsId) {
        return seckillGoodsMapper.queryLatestSeckill(goodsId);
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = seckillGoodsMapper.existsById(id);
        return count != null && count > 0;
    }

    @Override
    public boolean seckillWithPessimismLock(Long id) {
        Integer integer = seckillGoodsMapper.seckillWithPessimismLock(id);
        return integer != null && integer == 1;
    }

    @Override
    public Long queryStock(Long id) {
        return seckillGoodsMapper.queryStock(id);
    }

    @Override
    public boolean seckillWithOptimisticLock(Long id, Long stock) {
        Integer integer = seckillGoodsMapper.seckillWithOptimisticLock(id, stock);
        return integer != null && integer == 1;
    }

    @Override
    public void setStock(Long id, Integer stock) {
        seckillGoodsMapper.setStock(id, stock);
    }

    @Override
    public SeckillGoods queryById(Long id) {
        return cacheHelper.queryWithPerformanceFirst(RedisConstant.SECKILL_GOODS_ID_KEY, id, SeckillGoods.class,
                s -> {
                    List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.queryByNonNullFields(new SeckillGoods(id));
                    if (seckillGoodsList == null || seckillGoodsList.isEmpty()) {
                        return null;
                    }
                    return seckillGoodsList.get(0);
                }, RedisConstant.CACHE_DEFAULT_TTL);
    }

}
