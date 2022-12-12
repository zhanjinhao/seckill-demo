package cn.addenda.seckilldemo.vo;

import cn.addenda.businesseasy.json.LocalDateTimeStrDeSerializer;
import cn.addenda.businesseasy.json.LocalDateTimeStrSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author addenda
 * @datetime 2022/12/10 10:05
 */
@Setter
@Getter
@ToString
public class VParamSecKillGoods {

    private BigDecimal price;

    private Integer stock;

    @JsonDeserialize(using = LocalDateTimeStrDeSerializer.class)
    @JsonSerialize(using = LocalDateTimeStrSerializer.class)
    private LocalDateTime startDatetime;

    @JsonDeserialize(using = LocalDateTimeStrDeSerializer.class)
    @JsonSerialize(using = LocalDateTimeStrSerializer.class)
    private LocalDateTime endDatetime;

}
