package cn.addenda.seckilldemo.manager;

import cn.addenda.seckilldemo.po.Goods;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/12/7 21:15
 */
public interface GoodsManager {

    List<Goods> queryByNonNullFields(Goods goods);

    Goods queryByIdWithDB(Long id);

    Goods queryByIdWithPerformanceFirst(Long id);

    Goods queryByIdWithRTDataFirst(Long id);

}
