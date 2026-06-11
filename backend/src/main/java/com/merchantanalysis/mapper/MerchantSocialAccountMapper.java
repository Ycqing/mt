package com.merchantanalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.merchantanalysis.entity.MerchantSocialAccount;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MerchantSocialAccountMapper extends BaseMapper<MerchantSocialAccount> {

    List<MerchantSocialAccount> selectByMerchantId(@Param("merchantId") Long merchantId);

    void batchInsert(@Param("list") List<MerchantSocialAccount> list);
}
