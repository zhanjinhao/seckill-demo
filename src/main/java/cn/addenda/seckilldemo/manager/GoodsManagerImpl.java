package cn.addenda.seckilldemo.manager;

import cn.addenda.businesseasy.cache.CacheHelper;
import cn.addenda.seckilldemo.constant.RedisConstant;
import cn.addenda.seckilldemo.mapper.GoodsMapper;
import cn.addenda.seckilldemo.po.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/12/7 21:16
 */
@Component
public class GoodsManagerImpl implements GoodsManager {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CacheHelper cacheHelper;

    @Override
    public List<Goods> queryByNonNullFields(Goods goods) {
        return goodsMapper.queryOverviewByNonNullFields(goods);
    }

    @Override
    public Goods queryByIdWithDB(Long id) {
        List<Goods> goodsList = goodsMapper.queryByNonNullFields(new Goods(id));
        if (goodsList.isEmpty()) {
            return null;
        }
        return goodsList.get(0);
    }

    @Override
    public Goods queryByIdWithPerformanceFirst(Long id) {
        return cacheHelper.queryWithPerformanceFirst(RedisConstant.GOODS_ID_KEY, id, Goods.class,
                s -> queryByIdWithDB(id), RedisConstant.CACHE_DEFAULT_TTL);
    }

    @Override
    public Goods queryByIdWithRTDataFirst(Long id) {
        return cacheHelper.queryWithRTDataFirst(RedisConstant.GOODS_ID_KEY, id, Goods.class,
                s -> queryByIdWithDB(id), RedisConstant.CACHE_DEFAULT_TTL);
    }

}
