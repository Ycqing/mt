package com.merchantanalysis.dto.request;

import lombok.Data;
import java.io.Serializable;

@Data
public class FetchTaskRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String taskName;
    private Long merchantId;
    private String taskType;
    private String sourceUrl;
    private String params;
    private Integer priority;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String taskStatus;
}
