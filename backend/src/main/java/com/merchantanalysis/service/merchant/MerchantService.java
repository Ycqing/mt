package com.merchantanalysis.service.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.merchantanalysis.dto.request.MerchantQueryRequest;
import com.merchantanalysis.dto.request.MerchantRequest;
import com.merchantanalysis.dto.response.MerchantDetailVO;

import java.util.List;

public interface MerchantService {

    IPage<MerchantDetailVO> pageList(MerchantQueryRequest request);

    MerchantDetailVO getDetail(Long id);

    Long add(MerchantRequest request);

    void update(MerchantRequest request);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    List<MerchantDetailVO> listAll();

    void updateDataStatus(Long id, Integer dataStatus);
}
