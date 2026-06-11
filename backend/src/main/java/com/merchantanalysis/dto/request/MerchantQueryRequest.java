package com.merchantanalysis.dto.request;

import lombok.Data;
import java.io.Serializable;

@Data
public class MerchantQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;
    private Long industryId;
    private Integer isListed;
    private Integer dataStatus;
}
