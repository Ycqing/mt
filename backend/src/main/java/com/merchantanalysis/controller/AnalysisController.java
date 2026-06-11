package com.merchantanalysis.controller;

import com.merchantanalysis.common.Result;
import com.merchantanalysis.dto.request.CompareRequest;
import com.merchantanalysis.dto.response.ComparisonResultVO;
import com.merchantanalysis.service.analysis.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final ComparisonService comparisonService;

    @PostMapping("/compare")
    public Result<ComparisonResultVO> compare(@RequestBody CompareRequest request) {
        return Result.success(comparisonService.compare(request));
    }

    @GetMapping("/compare/{id}")
    public Result<ComparisonResultVO> getCompareResult(@PathVariable Long id) {
        return Result.success(comparisonService.getById(id));
    }

    @GetMapping("/compare/history")
    public Result<List<ComparisonResultVO>> getHistory(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(comparisonService.getHistory(pageNum, pageSize));
    }

    @DeleteMapping("/compare/{id}")
    public Result<Void> deleteCompare(@PathVariable Long id) {
        comparisonService.delete(id);
        return Result.success();
    }

    @GetMapping("/profile/{merchantId}")
    public Result<Map<String, Object>> getProfile(@PathVariable Long merchantId) {
        return Result.success(comparisonService.getProfile(merchantId));
    }
}
