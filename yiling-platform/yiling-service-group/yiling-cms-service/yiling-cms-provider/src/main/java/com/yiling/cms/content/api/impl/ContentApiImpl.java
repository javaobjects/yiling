package com.yiling.cms.content.api.impl;

import java.util.List;

import com.yiling.cms.content.bo.ContentBO;
import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.cms.content.service.ContentLikeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.ContentApi;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.DraftDTO;
import com.yiling.cms.content.service.ContentService;
import com.yiling.framework.common.base.request.BaseRequest;

/**
 * @author: gxl
 * @date: 2022/3/23
 */
@DubboService
public class ContentApiImpl implements ContentApi {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentLikeService contentLikeService;

    @Override
    public void addContent(AddContentRequest addContentRequest) {
        contentService.addContent(addContentRequest);
    }

    @Override
    public void updateContent(UpdateContentRequest updateContentRequest) {
        contentService.updateContent(updateContentRequest);
    }

    @Override
    public Page<ContentDTO> listPage(QueryContentPageRequest request) {
        return contentService.listPage(request);
    }

    @Override
    public List<ContentDTO> getByTitle(String title) {
        return contentService.getByTitle(title);
    }

    @Override
    public ContentDTO getContentById(Long id) {
        return contentService.getContentById(id);
    }

    @Override
    public List<ContentDTO> getContentInfoByIdList(List<Long> idList) {
        return contentService.getContentInfoByIdList(idList);
    }

    @Override
    public Page<AppContentDTO> listAppContentPage(QueryAppContentPageRequest request) {
        return contentService.listAppContentPage(request);
    }

    @Override
    public Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request) {
        return contentService.listAppContentPageBySql(request);
    }

    @Override
    public AppContentDetailDTO getContentDetail(Long id, LineEnum lineEnum) {
        return contentService.getContentDetail(id, lineEnum);
    }

    @Override
    public void deleteDraft(DeleteDraftRequest request) {
        contentService.deleteDraft(request);
    }

    @Override
    public List<DraftDTO> queryDraftList(BaseRequest request) {
        return contentService.queryDraftList(request);
    }

    @Override
    public Integer getLikeStatus(Long currentUserId, Long id) {
        return contentLikeService.getLikeStatus(currentUserId, id);
    }

    @Override
    public Long likeContent(LikeContentRequest request) {
        return contentLikeService.likeContent(request);
    }

    // @Override
    // public Page<ContentBO> clientContentPage(QueryContentPageRequest request) {
    //     return contentService.clientContentPage(request);
    // }

    // @Override
    // public Boolean contentRank(ContentRankRequest request) {
    //     return contentService.contentRank(request);
    // }
    //
    // @Override
    // public Boolean contentTop(ContentRankRequest request) {
    //     return contentService.contentTop(request);
    // }
}