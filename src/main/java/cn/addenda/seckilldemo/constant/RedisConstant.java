package cn.addenda.seckilldemo.constant;

/**
 * @author addenda
 * @datetime 2022/12/7 22:18
 */
public class RedisConstant {

    private RedisConstant() {

    }

    public static final Long CACHE_DEFAULT_TTL = 5 * 60 * 1000L;

    public static final String GOODS_ID_KEY = "seckilldemo:goods:id:";
    public static final String SECKILL_GOODS_ID_KEY = "seckilldemo:seckillGoods:id:";
    public static final String SECKILL_WITH_PESSIMISM_LOCK_AND_ONE_PERSON_ONE_ORDER_KEY = "seckilldemo:seckillGoods:plopoo:";
    public static final String SECKILL_WITH_OPTIMISTIC_LOCK_AND_ONE_PERSON_ONE_ORDER_KEY = "seckilldemo:seckillGoods:olopoo:";

    public static final String SECKILL_GOODS_STOCK_KEY = "seckilldemo:seckillGoods:stock:";
    public static final String SECKILL_GOODS_ORDER_KEY = "seckilldemo:seckillGoods:order:";

}
