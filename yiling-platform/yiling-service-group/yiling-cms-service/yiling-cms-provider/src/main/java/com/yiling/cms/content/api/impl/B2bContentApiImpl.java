package com.yiling.cms.content.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.B2bContentApi;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.AddHmcContentRequest;
import com.yiling.cms.content.dto.request.ContentRankRequest;
import com.yiling.cms.content.dto.request.ContentReferStatusRequest;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;
import com.yiling.cms.content.service.B2bContentService;

import lombok.RequiredArgsConstructor;

/**
 * 大运河内容管理API
 *
 * @author: yong.zhang
 * @date: 2023/6/26 0026
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class B2bContentApiImpl implements B2bContentApi {

    private final B2bContentService b2bContentService;
    
    @Override
    public void addContent(AddHmcContentRequest request) {
        b2bContentService.addContent(request);
    }

    @Override
    public Boolean contentRank(ContentRankRequest request) {
        return b2bContentService.contentRank(request);
    }

    @Override
    public Boolean updateReferStatus(ContentReferStatusRequest request) {
        return b2bContentService.updateReferStatus(request);
    }

    @Override
    public Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request) {
        return b2bContentService.listPage(request);
    }
}
