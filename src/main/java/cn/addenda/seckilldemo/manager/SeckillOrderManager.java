package cn.addenda.seckilldemo.manager;

import cn.addenda.seckilldemo.po.SeckillOrder;

/**
 * @author addenda
 * @datetime 2022/12/10 17:55
 */
public interface SeckillOrderManager {

    void insert(SeckillOrder seckillOrder);

    boolean exists(Long seckillGoodsId, String userId);

    void deleteBySeckillGoodsId(Long seckillGoodsId);

}
