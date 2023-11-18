package com.yiling.cms.content.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.SaContentApi;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.AddHmcContentRequest;
import com.yiling.cms.content.dto.request.ContentRankRequest;
import com.yiling.cms.content.dto.request.ContentReferStatusRequest;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;
import com.yiling.cms.content.service.SaContentService;

import lombok.RequiredArgsConstructor;

/**
 * 销售助手内容管理API
 *
 * @author: yong.zhang
 * @date: 2023/6/26 0026
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SaContentApiImpl implements SaContentApi {

    private final SaContentService saContentService;

    @Override
    public void addContent(AddHmcContentRequest request) {
        saContentService.addContent(request);
    }

    @Override
    public Boolean contentRank(ContentRankRequest request) {
        return saContentService.contentRank(request);
    }

    @Override
    public Boolean updateReferStatus(ContentReferStatusRequest request) {
        return saContentService.updateReferStatus(request);
    }

    @Override
    public Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request) {
        return saContentService.listPage(request);
    }
}
