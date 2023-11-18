package com.yiling.cms.read.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.read.api.MyReadApi;
import com.yiling.cms.read.dto.MyReadDTO;
import com.yiling.cms.read.dto.request.AddMyReadRequest;
import com.yiling.cms.read.dto.request.QueryMyReadPageRequest;
import com.yiling.cms.read.service.MyReadService;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@DubboService
public class MyReadApiImpl implements MyReadApi {

    @Autowired
    private MyReadService myReadService;

    @Override
    public void save(AddMyReadRequest request) {
        myReadService.add(request);
    }

    @Override
    public Page<MyReadDTO> queryPage(QueryMyReadPageRequest queryMyReadPageRequest) {
        return myReadService.queryPage(queryMyReadPageRequest);
    }
}