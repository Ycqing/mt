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
@TableName("merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String website;

    private Integer isListed;

    private String listingExchange;

    private String stockCode;

    private Long industryId;

    private String companyScale;

    private BigDecimal registeredCapital;

    private LocalDate establishedDate;

    private String businessDescription;

    private String advantages;

    private String productsInfo;

    private BigDecimal revenue;

    private Integer employeeCount;

    private String headquarters;

    private String sourceUrl;

    private Integer dataStatus;

    private LocalDateTime lastFetchTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
