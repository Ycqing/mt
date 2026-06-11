package com.merchantanalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.merchantanalysis.entity.MerchantReview;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MerchantReviewMapper extends BaseMapper<MerchantReview> {

    IPage<MerchantReview> selectPageByMerchantId(Page<MerchantReview> page, @Param("merchantId") Long merchantId);

    List<MerchantReview> selectByMerchantId(@Param("merchantId") Long merchantId);
}
