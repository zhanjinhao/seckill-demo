package cn.addenda.seckilldemo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author addenda
 * @datetime 2022/12/7 22:07
 */
@Setter
@Getter
@ToString
public class VResultGoodsOverview {

    private String name;

    private String title;

    private String img;

    private BigDecimal price;

}
