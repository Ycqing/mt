package com.merchantanalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.merchantanalysis.entity.FetchTask;
import org.apache.ibatis.annotations.Param;

public interface FetchTaskMapper extends BaseMapper<FetchTask> {

    IPage<FetchTask> selectPageList(Page<FetchTask> page, @Param("taskType") String taskType, @Param("taskStatus") String taskStatus);
}
