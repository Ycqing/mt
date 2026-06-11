package com.merchantanalysis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("merchant_operating_data")
public class MerchantOperatingData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long merchantId;

    private String platform;

    private LocalDate dataDate;

    private BigDecimal gmv;

    private BigDecimal salesAmount;

    private Long ordersCount;

    private BigDecimal avgOrderPrice;

    private BigDecimal conversionRate;

    private Long trafficCount;

    private Integer fansGrowth;

    private BigDecimal engagementRate;

    private BigDecimal liveSales;

    private BigDecimal adInvestment;

    private BigDecimal roi;

    private Integer hotProductsRank;

    private Integer categoryRank;

    private BigDecimal marketShare;

    private String rawData;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
