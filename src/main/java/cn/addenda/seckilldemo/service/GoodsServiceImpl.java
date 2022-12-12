package cn.addenda.seckilldemo.service;

import cn.addenda.se.result.ServiceResult;
import cn.addenda.seckilldemo.manager.GoodsManager;
import cn.addenda.seckilldemo.po.Goods;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/12/7 21:16
 */
@Component
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsManager goodsManager;

    @Override
    public ServiceResult<PageInfo<Goods>> pageQueryOverview(Integer pageNum, Integer pageSize, Goods goods) {
        try {
            PageMethod.startPage(pageNum, pageSize);
            List<Goods> query = goodsManager.queryByNonNullFields(goods);
            return ServiceResult.success(new PageInfo<>(query));
        } finally {
            PageMethod.clearPage();
        }
    }

    @Override
    public ServiceResult<Goods> queryByIdWithDB(Long id) {
        return ServiceResult.success(goodsManager.queryByIdWithDB(id));
    }

    @Override
    public ServiceResult<Goods> queryByIdWithPerformanceFirst(Long id) {
        return ServiceResult.success(goodsManager.queryByIdWithPerformanceFirst(id));
    }

    @Override
    public ServiceResult<Goods> queryByIdWithRTDataFirst(Long id) {
        return ServiceResult.success(goodsManager.queryByIdWithRTDataFirst(id));
    }

}
