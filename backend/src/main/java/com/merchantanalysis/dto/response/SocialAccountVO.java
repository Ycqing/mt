package com.merchantanalysis.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SocialAccountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long merchantId;
    private String merchantName;
    private String platform;
    private String platformName;
    private String accountName;
    private String accountId;
    private String accountUrl;
    private Long followersCount;
    private Long followingCount;
    private Integer postsCount;
    private Integer verificationStatus;
    private LocalDateTime lastFetchTime;
}
