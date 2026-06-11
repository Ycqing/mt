package com.merchantanalysis.service.report;

import com.merchantanalysis.dto.request.ReportGenerateRequest;
import com.merchantanalysis.dto.response.MerchantDetailVO;
import com.merchantanalysis.entity.MerchantReport;

import java.util.List;
import java.util.Map;

public interface ReportService {

    List<MerchantReport> list(Integer pageNum, Integer pageSize, String type, Long merchantId);

    MerchantReport getById(Long id);

    MerchantReport generateReport(ReportGenerateRequest request);

    void delete(Long id);

    String downloadReport(Long id);

    Map<String, Object> buildReportContent(ReportGenerateRequest request);

    String exportToExcel(MerchantDetailVO data, String type);

    String exportToPdf(MerchantDetailVO data, String type);
}
