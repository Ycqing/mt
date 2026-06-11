package com.merchantanalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.merchantanalysis.entity.MerchantFinancial;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MerchantFinancialMapper extends BaseMapper<MerchantFinancial> {

    IPage<MerchantFinancial> selectPageByMerchantId(Page<MerchantFinancial> page, @Param("merchantId") Long merchantId);

    List<MerchantFinancial> selectByMerchantId(@Param("merchantId") Long merchantId);

    List<MerchantFinancial> selectByMerchantIdAndYear(@Param("merchantId") Long merchantId, @Param("year") Integer year);
}
