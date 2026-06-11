package com.merchantanalysis.service.report;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.merchantanalysis.common.exception.BusinessException;
import com.merchantanalysis.dto.request.ReportGenerateRequest;
import com.merchantanalysis.dto.response.MerchantDetailVO;
import com.merchantanalysis.entity.MerchantReport;
import com.merchantanalysis.mapper.MerchantReportMapper;
import com.merchantanalysis.service.merchant.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final MerchantReportMapper reportMapper;
    private final MerchantService merchantService;

    @Override
    public List<MerchantReport> list(Integer pageNum, Integer pageSize, String type, Long merchantId) {
        return reportMapper.selectList(null);
    }

    @Override
    public MerchantReport getById(Long id) {
        return reportMapper.selectById(id);
    }

    @Override
    @Transactional
    public MerchantReport generateReport(ReportGenerateRequest request) {
        MerchantReport report = new MerchantReport();
        report.setMerchantId(request.getMerchantId());
        report.setReportName(request.getReportName());
        report.setReportType(request.getReportType());
        report.setReportPeriod(request.getReportPeriod());
        report.setStatus(0);
        reportMapper.insert(report);

        asyncGenerate(report.getId(), request);
        return report;
    }

    @Async("taskExecutor")
    public void asyncGenerate(Long reportId, ReportGenerateRequest request) {
        try {
            Map<String, Object> content = buildReportContent(request);
            MerchantReport report = reportMapper.selectById(reportId);
            report.setContent(JSON.toJSONString(content));
            report.setStatus(1);
            reportMapper.updateById(report);
        } catch (Exception e) {
            log.error("生成报告失败: reportId={}", reportId, e);
            MerchantReport report = reportMapper.selectById(reportId);
            report.setStatus(2);
            reportMapper.updateById(report);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        reportMapper.deleteById(id);
    }

    @Override
    public String downloadReport(Long id) {
        MerchantReport report = reportMapper.selectById(id);
        if (report == null) {
            throw new BusinessException("报告不存在");
        }
        if (report.getFilePath() == null) {
            throw new BusinessException("报告文件未生成");
        }
        return report.getFilePath();
    }

    @Override
    public Map<String, Object> buildReportContent(ReportGenerateRequest request) {
        Map<String, Object> content = new HashMap<>();

        if (request.getMerchantId() != null) {
            MerchantDetailVO merchant = merchantService.getDetail(request.getMerchantId());
            content.put("merchant", merchant);
        }

        content.put("reportType", request.getReportType());
        content.put("reportPeriod", request.getReportPeriod());
        content.put("modules", request.getModules());

        if (request.getModules() != null) {
            Map<String, Object> sections = new HashMap<>();
            for (String module : request.getModules()) {
                sections.put(module, buildSectionData(module, request));
            }
            content.put("sections", sections);
        }

        return content;
    }

    private Map<String, Object> buildSectionData(String module, ReportGenerateRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", getModuleTitle(module));
        data.put("data", new HashMap<>());
        data.put("conclusion", "数据已汇总生成。");
        return data;
    }

    private String getModuleTitle(String module) {
        switch (module) {
            case "summary": return "执行摘要";
            case "basic": return "基础信息";
            case "social": return "社交媒体分析";
            case "operating": return "经营数据分析";
            case "marketing": return "营销效果评估";
            case "comparison": return "竞品对比分析";
            case "review": return "产品评价分析";
            case "financial": return "财务数据";
            default: return module;
        }
    }

    @Override
    public String exportToExcel(MerchantDetailVO data, String type) {
        return "./reports/" + data.getName() + "_" + type + ".xlsx";
    }

    @Override
    public String exportToPdf(MerchantDetailVO data, String type) {
        return "./reports/" + data.getName() + "_" + type + ".pdf";
    }
}
