package com.merchantanalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.merchantanalysis.entity.ThirdPartyConfig;

public interface ThirdPartyConfigMapper extends BaseMapper<ThirdPartyConfig> {

    ThirdPartyConfig selectByPlatform(@Param("platform") String platform);
}
