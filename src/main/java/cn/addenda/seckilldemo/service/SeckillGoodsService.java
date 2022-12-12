package cn.addenda.seckilldemo.service;

import cn.addenda.se.result.ServiceResult;
import cn.addenda.seckilldemo.po.SeckillGoods;

/**
 * @author addenda
 * @datetime 2022/12/10 10:03
 */
public interface SeckillGoodsService {

    ServiceResult<SeckillGoods> createSeckillGoods(SeckillGoods seckillGoods);

    ServiceResult<SeckillGoods> queryLatestSeckill(Long goodsId);

    ServiceResult<Boolean> seckillWithPessimismLock(Long id);

    ServiceResult<Boolean> seckillWithPessimismLockAndOnePersonOneOrder(Long id);

    ServiceResult<Boolean> seckillWithOptimisticLock(Long id);

    ServiceResult<Boolean> seckillWithOptimisticLockAndOnePersonOneOrder(Long id);

    ServiceResult<Boolean> seckillWithRedisLua(Long id);

    ServiceResult<Boolean> resetSeckill(String ids, Integer stock);

}
