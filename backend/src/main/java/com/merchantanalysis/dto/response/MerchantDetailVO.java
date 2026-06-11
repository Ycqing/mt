package com.merchantanalysis.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class MerchantDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String website;
    private Integer isListed;
    private String listingExchange;
    private String stockCode;
    private Long industryId;
    private String industryName;
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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private List<SocialAccountVO> socialAccounts;
    private List<ProductVO> products;
    private List<FinancialVO> financials;
    private List<OperatingDataVO> operatingDataList;
    private List<ReviewVO> reviews;

    private Map<String, Object> statistics;
}
