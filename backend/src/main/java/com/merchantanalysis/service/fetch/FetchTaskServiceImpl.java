package com.merchantanalysis.service.fetch;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.merchantanalysis.common.enums.TaskStatus;
import com.merchantanalysis.dto.request.FetchTaskRequest;
import com.merchantanalysis.entity.FetchTask;
import com.merchantanalysis.mapper.FetchTaskMapper;
import com.merchantanalysis.mapper.MerchantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchTaskServiceImpl implements FetchTaskService {

    private final FetchTaskMapper fetchTaskMapper;
    private final MerchantMapper merchantMapper;

    @Override
    public IPage<FetchTask> pageList(FetchTaskRequest request) {
        Page<FetchTask> page = new Page<>(request.getPageNum(), request.getPageSize());
        return fetchTaskMapper.selectPageList(page, request.getTaskType(), request.getTaskStatus());
    }

    @Override
    public FetchTask getById(Long id) {
        return fetchTaskMapper.selectById(id);
    }

    @Override
    @Transactional
    public Long createTask(FetchTaskRequest request) {
        FetchTask task = new FetchTask();
        task.setTaskName(request.getTaskName());
        task.setMerchantId(request.getMerchantId());
        task.setTaskType(request.getTaskType());
        task.setTaskStatus(TaskStatus.PENDING.getCode());
        task.setPriority(request.getPriority() != null ? request.getPriority() : 3);
        task.setSourceUrl(request.getSourceUrl());
        task.setParams(request.getParams());
        task.setRetryCount(0);
        fetchTaskMapper.insert(task);
        return task.getId();
    }

    @Override
    @Transactional
    public void startTask(Long id) {
        FetchTask task = fetchTaskMapper.selectById(id);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        task.setTaskStatus(TaskStatus.RUNNING.getCode());
        task.setExecuteTime(LocalDateTime.now());
        fetchTaskMapper.updateById(task);
    }

    @Override
    @Transactional
    public void pauseTask(Long id) {
        FetchTask task = fetchTaskMapper.selectById(id);
        if (task != null) {
            task.setTaskStatus(TaskStatus.PAUSED.getCode());
            fetchTaskMapper.updateById(task);
        }
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        fetchTaskMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void batchStart(List<Long> ids) {
        ids.forEach(this::startTask);
    }

    @Override
    @Transactional
    public void batchPause(List<Long> ids) {
        ids.forEach(this::pauseTask);
    }

    @Override
    @Async("taskExecutor")
    public void executeTask(Long taskId) {
        FetchTask task = fetchTaskMapper.selectById(taskId);
        if (task == null) return;

        long startTime = System.currentTimeMillis();
        try {
            task.setTaskStatus(TaskStatus.RUNNING.getCode());
            fetchTaskMapper.updateById(task);

            String taskType = task.getTaskType();
            if ("website".equals(taskType)) {
                simulateFetch("官网数据采集");
            } else if ("financial".equals(taskType)) {
                simulateFetch("财报数据采集");
            } else if ("review".equals(taskType)) {
                simulateFetch("产品评价采集");
            } else if ("social".equals(taskType)) {
                simulateFetch("社交账号采集");
            } else if ("product".equals(taskType)) {
                simulateFetch("产品信息采集");
            }

            task.setTaskStatus(TaskStatus.SUCCESS.getCode());
            task.setResult("采集成功");
        } catch (Exception e) {
            log.error("任务执行失败: taskId={}", taskId, e);
            task.setTaskStatus(TaskStatus.FAILED.getCode());
            task.setResult("采集失败: " + e.getMessage());
        } finally {
            task.setCostMs(System.currentTimeMillis() - startTime);
            fetchTaskMapper.updateById(task);
        }
    }

    private void simulateFetch(String type) throws InterruptedException {
        Thread.sleep(100);
        log.info("执行: {}", type);
    }

    @Override
    public Map<String, Object> fetchWebsite(Long merchantId, String url) {
        Map<String, Object> result = new HashMap<>();
        result.put("merchantId", merchantId);
        result.put("url", url);
        result.put("status", "success");
        return result;
    }

    @Override
    public Map<String, Object> fetchFinancial(Long merchantId, Integer year) {
        Map<String, Object> result = new HashMap<>();
        result.put("merchantId", merchantId);
        result.put("year", year);
        result.put("status", "success");
        return result;
    }

    @Override
    public Map<String, Object> fetchReviews(String keyword, String platform, Integer pageNum) {
        Map<String, Object> result = new HashMap<>();
        result.put("keyword", keyword);
        result.put("platform", platform);
        result.put("pageNum", pageNum);
        result.put("total", 0);
        result.put("data", Collections.emptyList());
        return result;
    }
}
