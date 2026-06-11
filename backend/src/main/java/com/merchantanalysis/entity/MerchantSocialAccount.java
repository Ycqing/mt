package com.merchantanalysis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("merchant_social_account")
public class MerchantSocialAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long merchantId;

    private String platform;

    private String accountName;

    private String accountId;

    private String accountUrl;

    private Long followersCount;

    private Long followingCount;

    private Integer postsCount;

    private Integer verificationStatus;

    private LocalDateTime lastFetchTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
