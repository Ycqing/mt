package com.merchantanalysis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.merchantanalysis.common.Result;
import com.merchantanalysis.dto.request.MerchantQueryRequest;
import com.merchantanalysis.dto.request.MerchantRequest;
import com.merchantanalysis.dto.response.MerchantDetailVO;
import com.merchantanalysis.service.merchant.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping
    public Result<IPage<MerchantDetailVO>> pageList(MerchantQueryRequest request) {
        return Result.success(merchantService.pageList(request));
    }

    @GetMapping("/{id}")
    public Result<MerchantDetailVO> getDetail(@PathVariable Long id) {
        return Result.success(merchantService.getDetail(id));
    }

    @GetMapping("/list")
    public Result<List<MerchantDetailVO>> listAll() {
        return Result.success(merchantService.listAll());
    }

    @PostMapping
    public Result<Long> add(@Valid @RequestBody MerchantRequest request) {
        return Result.success(merchantService.add(request));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody MerchantRequest request) {
        request.setId(id);
        merchantService.update(request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        merchantService.delete(id);
        return Result.success();
    }

    @PostMapping("/batch-delete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        merchantService.batchDelete(ids);
        return Result.success();
    }

    @PutMapping("/{id}/data-status")
    public Result<Void> updateDataStatus(@PathVariable Long id, @RequestParam Integer dataStatus) {
        merchantService.updateDataStatus(id, dataStatus);
        return Result.success();
    }
}
