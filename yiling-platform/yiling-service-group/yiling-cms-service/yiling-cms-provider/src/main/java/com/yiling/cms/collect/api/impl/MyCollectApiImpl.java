package com.yiling.cms.collect.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.collect.api.MyCollectApi;
import com.yiling.cms.collect.dto.MyCollectDTO;
import com.yiling.cms.collect.dto.request.QueryCollectPageRequest;
import com.yiling.cms.collect.dto.request.QueryCollectRequest;
import com.yiling.cms.collect.dto.request.SaveCollectRequest;
import com.yiling.cms.collect.service.MyCollectService;

/**
 * 我的收藏
 * @author: gxl
 * @date: 2022/7/29
 */
@DubboService
public class MyCollectApiImpl implements MyCollectApi {

    @Autowired
    private MyCollectService myCollectService;

    @Override
    public void save(SaveCollectRequest request) {
        myCollectService.save(request);
    }

    @Override
    public Page<MyCollectDTO> queryPage(QueryCollectPageRequest request) {
        return myCollectService.queryPage(request);
    }

    @Override
    public MyCollectDTO getOne(QueryCollectRequest request) {
        return myCollectService.getOneCollect(request);
    }
}