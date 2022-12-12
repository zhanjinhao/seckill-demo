package cn.addenda.seckilldemo.mapper;

import cn.addenda.seckilldemo.po.Goods;

import java.util.List;

public interface GoodsMapper {

    List<Goods> queryByNonNullFields(Goods goods);

    List<Goods> queryOverviewByNonNullFields(Goods goods);
}
