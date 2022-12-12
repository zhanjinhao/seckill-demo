package cn.addenda.seckilldemo.service;

import cn.addenda.se.result.ServiceResult;
import cn.addenda.seckilldemo.po.Goods;
import com.github.pagehelper.PageInfo;

public interface GoodsService {

    ServiceResult<PageInfo<Goods>> pageQueryOverview(Integer pageNum, Integer pageSize, Goods goods);

    ServiceResult<Goods> queryByIdWithDB(Long id);

    ServiceResult<Goods> queryByIdWithPerformanceFirst(Long id);

    ServiceResult<Goods> queryByIdWithRTDataFirst(Long id);

}
