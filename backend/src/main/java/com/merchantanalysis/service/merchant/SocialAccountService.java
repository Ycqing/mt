package com.merchantanalysis.service.merchant;

import com.merchantanalysis.dto.response.SocialAccountVO;
import com.merchantanalysis.dto.response.ProductVO;
import com.merchantanalysis.entity.MerchantSocialAccount;

import java.util.List;

public interface SocialAccountService {

    List<SocialAccountVO> getByMerchantId(Long merchantId);

    Long add(Long merchantId, MerchantSocialAccount account);

    void update(MerchantSocialAccount account);

    void delete(Long id);

    void batchAdd(List<MerchantSocialAccount> accounts);

    void syncFromPlatform(Long merchantId, String platform);
}
