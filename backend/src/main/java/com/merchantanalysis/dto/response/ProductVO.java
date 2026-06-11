package com.merchantanalysis.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long merchantId;
    private String merchantName;
    private String productName;
    private String category;
    private BigDecimal price;
    private Long salesCount;
    private BigDecimal rating;
    private String productUrl;
    private String description;
    private String tags;
    private String source;
    private LocalDateTime updateTime;
}
