package com.merchantanalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.merchantanalysis.dto.request.MerchantQueryRequest;
import com.merchantanalysis.entity.Merchant;
import org.apache.ibatis.annotations.Param;

public interface MerchantMapper extends BaseMapper<Merchant> {

    IPage<Merchant> selectPageList(Page<Merchant> page, @Param("req") MerchantQueryRequest request);

}
