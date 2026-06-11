package com.merchantanalysis.controller;

import com.merchantanalysis.common.Result;
import com.merchantanalysis.dto.response.SocialAccountVO;
import com.merchantanalysis.entity.MerchantSocialAccount;
import com.merchantanalysis.service.merchant.SocialAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social-account")
@RequiredArgsConstructor
public class SocialAccountController {

    private final SocialAccountService socialAccountService;

    @GetMapping("/merchant/{merchantId}")
    public Result<List<SocialAccountVO>> getByMerchantId(@PathVariable Long merchantId) {
        return Result.success(socialAccountService.getByMerchantId(merchantId));
    }

    @PostMapping("/merchant/{merchantId}")
    public Result<Long> add(@PathVariable Long merchantId, @RequestBody MerchantSocialAccount account) {
        return Result.success(socialAccountService.add(merchantId, account));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody MerchantSocialAccount account) {
        account.setId(id);
        socialAccountService.update(account);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        socialAccountService.delete(id);
        return Result.success();
    }

    @PostMapping("/sync/{merchantId}")
    public Result<Void> sync(@PathVariable Long merchantId, @RequestParam String platform) {
        socialAccountService.syncFromPlatform(merchantId, platform);
        return Result.success();
    }
}
