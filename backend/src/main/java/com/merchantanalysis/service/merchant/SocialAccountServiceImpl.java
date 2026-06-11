package com.merchantanalysis.service.merchant;

import com.merchantanalysis.common.enums.Platform;
import com.merchantanalysis.common.exception.BusinessException;
import com.merchantanalysis.dto.response.SocialAccountVO;
import com.merchantanalysis.entity.MerchantSocialAccount;
import com.merchantanalysis.mapper.MerchantSocialAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialAccountServiceImpl implements SocialAccountService {

    private final MerchantSocialAccountMapper socialAccountMapper;

    @Override
    public List<SocialAccountVO> getByMerchantId(Long merchantId) {
        return socialAccountMapper.selectByMerchantId(merchantId)
                .stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long add(Long merchantId, MerchantSocialAccount account) {
        account.setMerchantId(merchantId);
        socialAccountMapper.insert(account);
        return account.getId();
    }

    @Override
    @Transactional
    public void update(MerchantSocialAccount account) {
        if (account.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        socialAccountMapper.updateById(account);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        socialAccountMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void batchAdd(List<MerchantSocialAccount> accounts) {
        if (accounts != null && !accounts.isEmpty()) {
            socialAccountMapper.batchInsert(accounts);
        }
    }

    @Override
    public void syncFromPlatform(Long merchantId, String platform) {
        log.info("同步社交账号数据: merchantId={}, platform={}", merchantId, platform);
    }

    private SocialAccountVO convert(MerchantSocialAccount a) {
        SocialAccountVO vo = new SocialAccountVO();
        vo.setId(a.getId());
        vo.setMerchantId(a.getMerchantId());
        vo.setPlatform(a.getPlatform());
        vo.setPlatformName(getPlatformName(a.getPlatform()));
        vo.setAccountName(a.getAccountName());
        vo.setAccountId(a.getAccountId());
        vo.setAccountUrl(a.getAccountUrl());
        vo.setFollowersCount(a.getFollowersCount());
        vo.setFollowingCount(a.getFollowingCount());
        vo.setPostsCount(a.getPostsCount());
        vo.setVerificationStatus(a.getVerificationStatus());
        vo.setLastFetchTime(a.getLastFetchTime());
        return vo;
    }

    private String getPlatformName(String code) {
        try {
            return Platform.fromCode(code).getDesc();
        } catch (Exception e) {
            return code;
        }
    }
}
