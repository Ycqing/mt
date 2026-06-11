package com.merchantanalysis.controller;

import com.merchantanalysis.common.Result;
import com.merchantanalysis.dto.request.ReportGenerateRequest;
import com.merchantanalysis.entity.MerchantReport;
import com.merchantanalysis.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public Result<List<MerchantReport>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long merchantId) {
        return Result.success(reportService.list(pageNum, pageSize, type, merchantId));
    }

    @GetMapping("/{id}")
    public Result<MerchantReport> getById(@PathVariable Long id) {
        return Result.success(reportService.getById(id));
    }

    @PostMapping("/generate")
    public Result<MerchantReport> generate(@RequestBody ReportGenerateRequest request) {
        return Result.success(reportService.generateReport(request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/download")
    public Result<String> download(@PathVariable Long id) {
        return Result.success(reportService.downloadReport(id));
    }
}
