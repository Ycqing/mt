package com.merchantanalysis.dto.request;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class ReportGenerateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long merchantId;
    private List<Long> merchantIds;
    private String reportType;
    private String reportName;
    private String reportPeriod;
    private List<String> modules;
    private String outputFormat;
    private String templateId;
}
