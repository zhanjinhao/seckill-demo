package cn.addenda.seckilldemo.manager;

import cn.addenda.seckilldemo.mapper.SeckillOrderMapper;
import cn.addenda.seckilldemo.po.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author addenda
 * @datetime 2022/12/10 17:55
 */
@Component
public class SeckillOrderManagerImpl implements SeckillOrderManager {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Override
    public void insert(SeckillOrder seckillOrder) {
        seckillOrderMapper.insert(seckillOrder);
    }

    @Override
    public boolean exists(Long seckillGoodsId, String userId) {
        Integer count = seckillOrderMapper.exists(seckillGoodsId, userId);
        return count != null && count > 0;
    }

    @Override
    public void deleteBySeckillGoodsId(Long seckillGoodsId) {
        seckillOrderMapper.deleteBySeckillGoodsId(seckillGoodsId);
    }
}
