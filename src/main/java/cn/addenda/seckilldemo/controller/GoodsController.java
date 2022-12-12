package cn.addenda.seckilldemo.controller;

import cn.addenda.businesseasy.util.BEAssertUtils;
import cn.addenda.se.result.ControllerResult;
import cn.addenda.se.utils.SeBeanUtil;
import cn.addenda.seckilldemo.po.Goods;
import cn.addenda.seckilldemo.service.GoodsService;
import cn.addenda.seckilldemo.vo.VParamGoods;
import cn.addenda.seckilldemo.vo.VResultGoodsOverview;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author addenda
 * @datetime 2022/12/7 21:14
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/pageQueryOverview")
    public ControllerResult<PageInfo<VResultGoodsOverview>> pageQueryOverview(
            @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestBody VParamGoods goods) {
        BEAssertUtils.notNull(pageNum, "pageNum");
        BEAssertUtils.notNull(pageSize, "pageSize");
        BEAssertUtils.notNull(goods);
        BEAssertUtils.notApplied(goods.getTitle(), "title");
        BEAssertUtils.notApplied(goods.getImg(), "img");
        BEAssertUtils.notApplied(goods.getDetail(), "detail");
        BEAssertUtils.notApplied(goods.getPrice(), "price");

        return ControllerResult.create(
                goodsService.pageQueryOverview(pageNum, pageSize, SeBeanUtil.copyProperties(goods, new Goods())),
                goodsPageInfo -> {
                    PageInfo<VResultGoodsOverview> result = SeBeanUtil.copyProperties(goodsPageInfo, new PageInfo<>());
                    List<VResultGoodsOverview> list = new ArrayList<>();
                    for (Goods goodsItem : goodsPageInfo.getList()) {
                        list.add(SeBeanUtil.copyProperties(goodsItem, new VResultGoodsOverview()));
                    }
                    result.setList(list);
                    return result;
                });
    }

    @GetMapping("/queryByIdWithDB")
    public ControllerResult<Goods> queryByIdWithDB(@RequestParam("id") Long id) {
        BEAssertUtils.notNull(id, "id");

        return ControllerResult.create(goodsService.queryByIdWithDB(id));
    }

    @GetMapping("/queryByIdWithPerformanceFirst")
    public ControllerResult<Goods> queryByIdWithPerformanceFirst(@RequestParam("id") Long id) {
        BEAssertUtils.notNull(id, "id");

        return ControllerResult.create(goodsService.queryByIdWithPerformanceFirst(id));
    }

    @GetMapping("/queryByIdWithRTDataFirst")
    public ControllerResult<Goods> queryByIdWithRTDataFirst(@RequestParam("id") Long id) {
        BEAssertUtils.notNull(id, "id");

        return ControllerResult.create(goodsService.queryByIdWithRTDataFirst(id));
    }

}
