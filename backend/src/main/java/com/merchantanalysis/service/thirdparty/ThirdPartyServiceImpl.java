package com.merchantanalysis.service.thirdparty;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.merchantanalysis.entity.MerchantOperatingData;
import com.merchantanalysis.entity.ThirdPartyConfig;
import com.merchantanalysis.mapper.MerchantOperatingDataMapper;
import com.merchantanalysis.mapper.ThirdPartyConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final ThirdPartyConfigMapper configMapper;
    private final MerchantOperatingDataMapper operatingDataMapper;

    @Override
    public Map<String, Object> getProductRankings(String platform, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("platform", platform);
        result.put("data", Collections.emptyList());
        result.put("total", 0);
        log.info("获取商品排行榜: platform={}", platform);
        return result;
    }

    @Override
    public Map<String, Object> getBrandRankings(String platform, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("platform", platform);
        result.put("data", Collections.emptyList());
        return result;
    }

    @Override
    public Map<String, Object> getSalesData(String platform, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("platform", platform);
        result.put("data", Collections.emptyList());
        return result;
    }

    @Override
    public Map<String, Object> searchReports(String keyword) {
        Map<String, Object> result = new HashMap<>();
        result.put("keyword", keyword);
        result.put("data", Collections.emptyList());
        return result;
    }

    @Override
    public Map<String, Object> getQuota(String platform) {
        LambdaQueryWrapper<ThirdPartyConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ThirdPartyConfig::getPlatform, platform);
        ThirdPartyConfig config = configMapper.selectOne(wrapper);

        Map<String, Object> quota = new HashMap<>();
        if (config != null) {
            quota.put("platform", platform);
            quota.put("monthlyQuota", config.getMonthlyQuota());
            quota.put("monthlyUsed", config.getMonthlyUsed());
            quota.put("remaining", config.getMonthlyQuota() - config.getMonthlyUsed());
        } else {
            quota.put("platform", platform);
            quota.put("status", "not_configured");
        }
        return quota;
    }

    @Override
    public void syncOperatingData(Long merchantId, String platform) {
        log.info("同步经营数据: merchantId={}, platform={}", merchantId, platform);

        MerchantOperatingData data = new MerchantOperatingData();
        data.setMerchantId(merchantId);
        data.setPlatform(platform);
        data.setDataDate(LocalDate.now());
        operatingDataMapper.insert(data);
    }
}
