package com.merchantanalysis.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ReviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long merchantId;
    private String merchantName;
    private Long productId;
    private String productName;
    private String platform;
    private String platformName;
    private String reviewerNickname;
    private Integer rating;
    private String content;
    private Integer likeCount;
    private Integer replyCount;
    private String mediaUrls;
    private String sentiment;
    private String sentimentName;
    private String keywords;
    private LocalDate reviewDate;
    private String sourceUrl;
}
