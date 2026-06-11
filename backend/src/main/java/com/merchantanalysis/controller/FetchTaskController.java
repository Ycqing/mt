package com.merchantanalysis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.merchantanalysis.common.Result;
import com.merchantanalysis.dto.request.FetchTaskRequest;
import com.merchantanalysis.entity.FetchTask;
import com.merchantanalysis.service.fetch.FetchTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fetch-task")
@RequiredArgsConstructor
public class FetchTaskController {

    private final FetchTaskService fetchTaskService;

    @GetMapping
    public Result<IPage<FetchTask>> pageList(FetchTaskRequest request) {
        return Result.success(fetchTaskService.pageList(request));
    }

    @GetMapping("/{id}")
    public Result<FetchTask> getById(@PathVariable Long id) {
        return Result.success(fetchTaskService.getById(id));
    }

    @PostMapping
    public Result<Long> create(@RequestBody FetchTaskRequest request) {
        return Result.success(fetchTaskService.createTask(request));
    }

    @PostMapping("/{id}/start")
    public Result<Void> start(@PathVariable Long id) {
        fetchTaskService.startTask(id);
        return Result.success();
    }

    @PostMapping("/{id}/pause")
    public Result<Void> pause(@PathVariable Long id) {
        fetchTaskService.pauseTask(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        fetchTaskService.deleteTask(id);
        return Result.success();
    }

    @PostMapping("/batch-start")
    public Result<Void> batchStart(@RequestBody List<Long> ids) {
        fetchTaskService.batchStart(ids);
        return Result.success();
    }

    @PostMapping("/batch-pause")
    public Result<Void> batchPause(@RequestBody List<Long> ids) {
        fetchTaskService.batchPause(ids);
        return Result.success();
    }

    @PostMapping("/execute/{id}")
    public Result<Void> execute(@PathVariable Long id) {
        fetchTaskService.executeTask(id);
        return Result.success();
    }
}
