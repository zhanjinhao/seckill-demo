package cn.addenda.seckilldemo.manager;

import cn.addenda.seckilldemo.po.SeckillGoods;

import java.time.LocalDateTime;

public interface SeckillGoodsManager {

    boolean datetimeConflict(Long goodsId, LocalDateTime startDatetime, LocalDateTime endDatetime);

    void insert(SeckillGoods seckillGoods);

    SeckillGoods queryLatestSeckill(Long goodsId);

    boolean existsById(Long id);

    boolean seckillWithPessimismLock(Long id);

    Long queryStock(Long id);

    boolean seckillWithOptimisticLock(Long id, Long stock);

    void setStock(Long id, Integer stock);

    SeckillGoods queryById(Long id);

}
