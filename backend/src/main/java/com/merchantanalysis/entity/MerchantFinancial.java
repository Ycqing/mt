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
@TableName("merchant_financial")
public class MerchantFinancial implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long merchantId;

    private Integer reportYear;

    private String reportPeriod;

    private LocalDate reportDate;

    private BigDecimal revenue;

    private BigDecimal netProfit;

    private BigDecimal grossMargin;

    private BigDecimal operatingMargin;

    private BigDecimal totalAssets;

    private BigDecimal totalLiabilities;

    private BigDecimal roe;

    private BigDecimal eps;

    private String source;

    private String sourceUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
