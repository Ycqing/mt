package com.merchantanalysis.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OperatingDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long merchantId;
    private String merchantName;
    private String platform;
    private String platformName;
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
}
