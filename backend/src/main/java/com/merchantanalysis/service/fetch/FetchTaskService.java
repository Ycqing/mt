package com.merchantanalysis.service.fetch;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.merchantanalysis.dto.request.FetchTaskRequest;
import com.merchantanalysis.entity.FetchTask;

import java.util.Map;

public interface FetchTaskService {

    IPage<FetchTask> pageList(FetchTaskRequest request);

    FetchTask getById(Long id);

    Long createTask(FetchTaskRequest request);

    void startTask(Long id);

    void pauseTask(Long id);

    void deleteTask(Long id);

    void batchStart(List<Long> ids);

    void batchPause(List<Long> ids);

    void executeTask(Long taskId);

    Map<String, Object> fetchWebsite(Long merchantId, String url);

    Map<String, Object> fetchFinancial(Long merchantId, Integer year);

    Map<String, Object> fetchReviews(String keyword, String platform, Integer pageNum);
}
