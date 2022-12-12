package cn.addenda.seckilldemo.mapper;

import cn.addenda.seckilldemo.po.SeckillOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @author addenda
 * @datetime 2022/12/10 17:53
 */
public interface SeckillOrderMapper {

    void insert(SeckillOrder seckillOrder);

    Integer exists(@Param("seckillGoodsId") Long seckillGoodsId, @Param("userId") String userId);

    void deleteBySeckillGoodsId(@Param("seckillGoodsId") Long seckillGoodsId);

}
