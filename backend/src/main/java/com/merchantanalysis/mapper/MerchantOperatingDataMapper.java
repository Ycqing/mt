package com.merchantanalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.merchantanalysis.entity.MerchantOperatingData;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

public interface MerchantOperatingDataMapper extends BaseMapper<MerchantOperatingData> {

    IPage<MerchantOperatingData> selectPageByMerchantId(Page<MerchantOperatingData> page, @Param("merchantId") Long merchantId);

    List<MerchantOperatingData> selectByMerchantId(@Param("merchantId") Long merchantId);

    List<MerchantOperatingData> selectByMerchantIdAndDateRange(
            @Param("merchantId") Long merchantId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
