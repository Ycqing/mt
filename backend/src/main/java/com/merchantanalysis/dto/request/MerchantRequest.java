package com.merchantanalysis.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MerchantRequest implements Serializable {

    private Long id;

    @NotBlank(message = "商家名称不能为空")
    @Size(min = 2, max = 100, message = "商家名称长度为2-100个字符")
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
}
