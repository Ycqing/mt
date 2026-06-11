package com.merchantanalysis.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FinancialVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long merchantId;
    private String merchantName;
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
}
