package com.merchantanalysis.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ComparisonResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private List<MerchantBaseVO> merchants;
    private List<String> dimensions;
    private Map<String, List<DimensionValueVO>> dimensionData;
    private List<RadarChartVO> radarData;
    private Map<String, Object> summary;
    private String createdBy;
    private String createTime;
}

@Data
class MerchantBaseVO implements Serializable {
    private Long id;
    private String name;
    private String industryName;
}

@Data
class DimensionValueVO implements Serializable {
    private Long merchantId;
    private String merchantName;
    private Object value;
    private Double changeRate;
}

@Data
class RadarChartVO implements Serializable {
    private String name;
    private Map<String, Double> values;
}
