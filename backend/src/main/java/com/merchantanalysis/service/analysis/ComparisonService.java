package com.merchantanalysis.service.analysis;

import com.merchantanalysis.dto.request.CompareRequest;
import com.merchantanalysis.dto.response.ComparisonResultVO;

import java.util.List;
import java.util.Map;

public interface ComparisonService {

    ComparisonResultVO compare(CompareRequest request);

    ComparisonResultVO getById(Long id);

    List<ComparisonResultVO> getHistory(Integer pageNum, Integer pageSize);

    void delete(Long id);

    Map<String, Object> getProfile(Long merchantId);
}
