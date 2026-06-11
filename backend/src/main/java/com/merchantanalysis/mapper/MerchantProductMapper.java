package com.merchantanalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.merchantanalysis.entity.MerchantProduct;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MerchantProductMapper extends BaseMapper<MerchantProduct> {

    List<MerchantProduct> selectByMerchantId(@Param("merchantId") Long merchantId);
}
