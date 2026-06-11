package com.merchantanalysis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("merchant_review")
public class MerchantReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long merchantId;

    private Long productId;

    private String platform;

    private String reviewerNickname;

    private Integer rating;

    private String content;

    private Integer likeCount;

    private Integer replyCount;

    private String mediaUrls;

    private String sentiment;

    private String keywords;

    private LocalDate reviewDate;

    private String sourceUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
