package cn.addenda.seckilldemo.controller;

import cn.addenda.businesseasy.util.BEAssertUtils;
import cn.addenda.se.result.ControllerResult;
import cn.addenda.se.utils.SeBeanUtil;
import cn.addenda.seckilldemo.po.SeckillGoods;
import cn.addenda.seckilldemo.service.SeckillGoodsService;
import cn.addenda.seckilldemo.vo.VParamSecKillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author addenda
 * @datetime 2022/12/9 19:18
 */
@RestController
@RequestMapping("/seckill")
public class SeckillGoodsController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @PostMapping("createSeckillGoods")
    public ControllerResult<SeckillGoods> createSeckillGoods(@RequestParam("goodsId") Long goodsId, @RequestBody VParamSecKillGoods secKillGoods) {
        BEAssertUtils.notNull(goodsId, "goodsId");
        BEAssertUtils.notNull(secKillGoods);
        BEAssertUtils.notNull(secKillGoods.getPrice(), "price");
        BEAssertUtils.notNull(secKillGoods.getStock(), "stock");
        BEAssertUtils.notNull(secKillGoods.getStartDatetime(), "startDatetime");
        BEAssertUtils.notNull(secKillGoods.getEndDatetime(), "endDatetime");

        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setGoodsId(goodsId);
        return ControllerResult.create(seckillGoodsService.createSeckillGoods(SeBeanUtil.copyPropertiesIgnoreNull(secKillGoods, seckillGoods)));
    }

    @GetMapping("queryLatestSeckill")
    public ControllerResult<SeckillGoods> queryLatestSeckill(@RequestParam("goodsId") Long goodsId) {
        BEAssertUtils.notNull(goodsId, "goodsId");

        return ControllerResult.create(seckillGoodsService.queryLatestSeckill(goodsId));
    }

    @PostMapping("/seckillWithPessimismLock")
    public ControllerResult<Boolean> seckillWithPessimismLock(@RequestParam("id") Long id) {
        BEAssertUtils.notNull(id, "id");

        return ControllerResult.create(seckillGoodsService.seckillWithPessimismLock(id));
    }

    @PostMapping("/seckillWithPessimismLockAndOnePersonOneOrder")
    public ControllerResult<Boolean> seckillWithPessimismLockAndOnePersonOneOrder(@RequestParam("id") Long id) {
        BEAssertUtils.notNull(id, "id");

        return ControllerResult.create(seckillGoodsService.seckillWithPessimismLockAndOnePersonOneOrder(id));
    }

    @PostMapping("/seckillWithOptimisticLock")
    public ControllerResult<Boolean> seckillWithOptimisticLock(@RequestParam("id") Long id) {
        BEAssertUtils.notNull(id, "id");

        return ControllerResult.create(seckillGoodsService.seckillWithOptimisticLock(id));
    }

    @PostMapping("/seckillWithOptimisticLockAndOnePersonOneOrder")
    public ControllerResult<Boolean> seckillWithOptimisticLockAndOnePersonOneOrder(@RequestParam("id") Long id) {
        BEAssertUtils.notNull(id, "id");

        return ControllerResult.create(seckillGoodsService.seckillWithOptimisticLockAndOnePersonOneOrder(id));
    }

    @PostMapping("/seckillWithRedisLua")
    public ControllerResult<Boolean> seckillWithRedisLua(@RequestParam("id") Long id) {
        BEAssertUtils.notNull(id, "id");

        return ControllerResult.create(seckillGoodsService.seckillWithRedisLua(id));
    }

    @PostMapping("/resetSeckill")
    public ControllerResult<Boolean> resetSeckill(@RequestParam("id") String ids, @RequestBody Integer stock) {
        BEAssertUtils.notNull(ids, "ids");
        BEAssertUtils.notNull(stock);

        return ControllerResult.create(seckillGoodsService.resetSeckill(ids, stock));
    }

}
