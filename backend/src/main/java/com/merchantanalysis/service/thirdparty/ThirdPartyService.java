package com.merchantanalysis.service.thirdparty;

import java.util.Map;

public interface ThirdPartyService {

    Map<String, Object> getProductRankings(String platform, Map<String, Object> params);

    Map<String, Object> getBrandRankings(String platform, Map<String, Object> params);

    Map<String, Object> getSalesData(String platform, Map<String, Object> params);

    Map<String, Object> searchReports(String keyword);

    Map<String, Object> getQuota(String platform);

    void syncOperatingData(Long merchantId, String platform);
}
