package com.merchantanalysis.service.analysis;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.merchantanalysis.common.exception.BusinessException;
import com.merchantanalysis.dto.request.CompareRequest;
import com.merchantanalysis.dto.response.*;
import com.merchantanalysis.entity.Merchant;
import com.merchantanalysis.entity.MerchantComparison;
import com.merchantanalysis.entity.MerchantFinancial;
import com.merchantanalysis.entity.MerchantOperatingData;
import com.merchantanalysis.entity.MerchantSocialAccount;
import com.merchantanalysis.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComparisonServiceImpl implements ComparisonService {

    private final MerchantMapper merchantMapper;
    private final MerchantComparisonMapper comparisonMapper;
    private final MerchantFinancialMapper financialMapper;
    private final MerchantOperatingDataMapper operatingDataMapper;
    private final MerchantSocialAccountMapper socialAccountMapper;

    @Override
    @Transactional
    public ComparisonResultVO compare(CompareRequest request) {
        ComparisonResultVO result = new ComparisonResultVO();

        List<Long> merchantIds = JSON.parseArray(request.getMerchantIds(), Long.class);
        List<Merchant> merchants = merchantMapper.selectBatchIds(merchantIds);

        List<MerchantBaseVO> merchantVOs = merchants.stream().map(m -> {
            MerchantBaseVO vo = new MerchantBaseVO();
            vo.setId(m.getId());
            vo.setName(m.getName());
            return vo;
        }).collect(Collectors.toList());
        result.setMerchants(merchantVOs);

        List<String> dimensions = JSON.parseArray(request.getCompareDimensions(), String.class);
        result.setDimensions(dimensions);

        Map<String, List<DimensionValueVO>> dimensionData = new HashMap<>();
        for (String dim : dimensions) {
            dimensionData.put(dim, buildDimensionValues(merchants, dim));
        }
        result.setDimensionData(dimensionData);

        Map<String, Object> summary = new HashMap<>();
        summary.put("total_merchants", merchants.size());
        summary.put("top_performer", findTopPerformer(merchants, dimensionData));
        summary.put("conclusion", generateConclusion(merchants, dimensionData));
        result.setSummary(summary);

        MerchantComparison comparison = new MerchantComparison();
        comparison.setName(request.getName());
        comparison.setMerchantIds(request.getMerchantIds());
        comparison.setCompareDimensions(request.getCompareDimensions());
        comparison.setSummary(JSON.toJSONString(summary));
        comparison.setCreatedBy(request.getCreatedBy());
        comparisonMapper.insert(comparison);
        result.setId(comparison.getId());

        return result;
    }

    @Override
    public ComparisonResultVO getById(Long id) {
        MerchantComparison comparison = comparisonMapper.selectById(id);
        if (comparison == null) {
            throw new BusinessException("对比记录不存在");
        }
        return buildResultFromEntity(comparison);
    }

    @Override
    public List<ComparisonResultVO> getHistory(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<MerchantComparison> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MerchantComparison::getCreateTime);
        List<MerchantComparison> list = comparisonMapper.selectList(wrapper);
        return list.stream().map(this::buildResultFromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        comparisonMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getProfile(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }

        Map<String, Object> profile = new HashMap<>();
        profile.put("basic", merchant);

        List<MerchantFinancial> financials = financialMapper.selectByMerchantId(merchantId);
        profile.put("financial", financials);

        List<MerchantOperatingData> operatingData = operatingDataMapper.selectByMerchantId(merchantId);
        profile.put("operating", operatingData);

        List<MerchantSocialAccount> socialAccounts = socialAccountMapper.selectByMerchantId(merchantId);
        profile.put("social", socialAccounts);

        return profile;
    }

    private List<DimensionValueVO> buildDimensionValues(List<Merchant> merchants, String dimension) {
        List<DimensionValueVO> values = new ArrayList<>();
        for (Merchant m : merchants) {
            DimensionValueVO vo = new DimensionValueVO();
            vo.setMerchantId(m.getId());
            vo.setMerchantName(m.getName());
            vo.setValue(getDimensionValue(m, dimension));
            values.add(vo);
        }
        return values;
    }

    private Object getDimensionValue(Merchant merchant, String dimension) {
        switch (dimension) {
            case "revenue":
                return merchant.getRevenue() != null ? merchant.getRevenue() : BigDecimal.ZERO;
            case "employees":
                return merchant.getEmployeeCount() != null ? merchant.getEmployeeCount() : 0;
            case "scale":
                return merchant.getCompanyScale() != null ? merchant.getCompanyScale() : "未知";
            default:
                return 0;
        }
    }

    private String findTopPerformer(List<Merchant> merchants, Map<String, List<DimensionValueVO>> data) {
        if (merchants.isEmpty()) return "";
        return merchants.get(0).getName();
    }

    private String generateConclusion(List<Merchant> merchants, Map<String, List<DimensionValueVO>> data) {
        return String.format("本次对比分析了%d个商家的核心指标，各维度表现详见下方表格。", merchants.size());
    }

    private ComparisonResultVO buildResultFromEntity(MerchantComparison c) {
        ComparisonResultVO vo = new ComparisonResultVO();
        vo.setId(c.getId());
        vo.setName(c.getName());
        vo.setSummary(JSON.parseObject(c.getSummary()));
        vo.setCreatedBy(c.getCreatedBy());
        vo.setCreateTime(c.getCreateTime() != null ? c.getCreateTime().toString() : "");
        return vo;
    }
}
