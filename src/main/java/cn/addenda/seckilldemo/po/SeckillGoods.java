package cn.addenda.seckilldemo.po;

import cn.addenda.businesseasy.json.LocalDateTimeStrDeSerializer;
import cn.addenda.businesseasy.json.LocalDateTimeStrSerializer;
import cn.addenda.me.fieldfilling.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author addenda
 * @datetime 2022/12/7 21:03
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class SeckillGoods extends BaseEntity {

    private Long id;

    private Long goodsId;

    private BigDecimal price;

    private Integer stock;

    @JsonDeserialize(using = LocalDateTimeStrDeSerializer.class)
    @JsonSerialize(using = LocalDateTimeStrSerializer.class)
    private LocalDateTime startDatetime;

    @JsonDeserialize(using = LocalDateTimeStrDeSerializer.class)
    @JsonSerialize(using = LocalDateTimeStrSerializer.class)
    private LocalDateTime endDatetime;

    public SeckillGoods(Long id) {
        this.id = id;
    }
}
