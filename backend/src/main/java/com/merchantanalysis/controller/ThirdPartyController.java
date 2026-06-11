package com.merchantanalysis.controller;

import com.merchantanalysis.common.Result;
import com.merchantanalysis.service.thirdparty.ThirdPartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/third-party")
@RequiredArgsConstructor
public class ThirdPartyController {

    private final ThirdPartyService thirdPartyService;

    @GetMapping("/quota/{platform}")
    public Result<Map<String, Object>> getQuota(@PathVariable String platform) {
        return Result.success(thirdPartyService.getQuota(platform));
    }

    @GetMapping("/products/{platform}")
    public Result<Map<String, Object>> getProductRankings(
            @PathVariable String platform,
            @RequestParam Map<String, Object> params) {
        return Result.success(thirdPartyService.getProductRankings(platform, params));
    }

    @GetMapping("/brands/{platform}")
    public Result<Map<String, Object>> getBrandRankings(
            @PathVariable String platform,
            @RequestParam Map<String, Object> params) {
        return Result.success(thirdPartyService.getBrandRankings(platform, params));
    }

    @GetMapping("/sales/{platform}")
    public Result<Map<String, Object>> getSalesData(
            @PathVariable String platform,
            @RequestParam Map<String, Object> params) {
        return Result.success(thirdPartyService.getSalesData(platform, params));
    }

    @GetMapping("/reports")
    public Result<Map<String, Object>> searchReports(@RequestParam String keyword) {
        return Result.success(thirdPartyService.searchReports(keyword));
    }

    @PostMapping("/sync/{merchantId}")
    public Result<Void> syncOperatingData(
            @PathVariable Long merchantId,
            @RequestParam String platform) {
        thirdPartyService.syncOperatingData(merchantId, platform);
        return Result.success();
    }
}
