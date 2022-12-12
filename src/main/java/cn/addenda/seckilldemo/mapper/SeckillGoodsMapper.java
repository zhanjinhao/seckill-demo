package cn.addenda.seckilldemo.mapper;

import cn.addenda.seckilldemo.po.SeckillGoods;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SeckillGoodsMapper {

    Integer datetimeConflict(@Param("goodsId") Long goodsId,
                             @Param("startDatetime") LocalDateTime startDatetime,
                             @Param("endDatetime") LocalDateTime endDatetime);

    void insert(SeckillGoods seckillGoods);

    SeckillGoods queryLatestSeckill(@Param("goodsId") Long goodsId);

    Integer existsById(@Param("id") Long id);

    Integer seckillWithPessimismLock(@Param("id") Long id);

    Long queryStock(@Param("id") Long id);

    Integer seckillWithOptimisticLock(@Param("id") Long id, @Param("stock") Long stock);

    void setStock(@Param("id") Long id, @Param("stock") Integer stock);

    List<SeckillGoods> queryByNonNullFields(SeckillGoods seckillGoods);

}
