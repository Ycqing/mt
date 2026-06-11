package com.merchantanalysis.controller;

import com.merchantanalysis.common.Result;
import com.merchantanalysis.service.fetch.FetchTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class FetchDataController {

    private final FetchTaskService fetchTaskService;

    @PostMapping("/website")
    public Result<Map<String, Object>> fetchWebsite(@RequestBody Map<String, Object> params) {
        Long merchantId = params.get("merchantId") != null ? Long.valueOf(params.get("merchantId").toString()) : null;
        String url = params.get("url") != null ? params.get("url").toString() : null;
        return Result.success(fetchTaskService.fetchWebsite(merchantId, url));
    }

    @PostMapping("/financial")
    public Result<Map<String, Object>> fetchFinancial(@RequestBody Map<String, Object> params) {
        Long merchantId = params.get("merchantId") != null ? Long.valueOf(params.get("merchantId").toString()) : null;
        Integer year = params.get("year") != null ? Integer.valueOf(params.get("year").toString()) : null;
        return Result.success(fetchTaskService.fetchFinancial(merchantId, year));
    }

    @PostMapping("/reviews")
    public Result<Map<String, Object>> fetchReviews(@RequestBody Map<String, Object> params) {
        String keyword = params.get("keyword") != null ? params.get("keyword").toString() : null;
        String platform = params.get("platform") != null ? params.get("platform").toString() : null;
        Integer pageNum = params.get("pageNum") != null ? Integer.valueOf(params.get("pageNum").toString()) : 1;
        return Result.success(fetchTaskService.fetchReviews(keyword, platform, pageNum));
    }
}
