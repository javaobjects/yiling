package com.yiling.cms.content.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.HMCContentApi;
import com.yiling.cms.content.bo.ContentBO;
import com.yiling.cms.content.dto.*;
import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.content.entity.HmcContentDO;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.cms.content.service.ContentLikeService;
import com.yiling.cms.content.service.ContentService;
import com.yiling.cms.content.service.HmcContentService;
import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: fan.shen
 * @date: 2022/3/23
 */
@DubboService
public class HMCContentApiImpl implements HMCContentApi {

    @Autowired
    private ContentService contentService;

    @Autowired
    private HmcContentService hmcContentService;

    @Autowired
    private ContentLikeService contentLikeService;

    @Override
    public void addContent(AddHmcContentRequest addContentRequest) {
        hmcContentService.addContent(addContentRequest);
    }

    @Override
    public void updateContent(UpdateContentRequest updateContentRequest) {
        contentService.updateContent(updateContentRequest);
    }

    @Override
    public Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request) {
        return hmcContentService.listPage(request);
    }

    @Override
    public ContentDTO getContentById(Long id) {
        return contentService.getContentById(id);
    }

    @Override
    public List<HMCContentDTO> getContentByContentIdList(List<Long> idList) {
        List<HmcContentDO> hmcContentDOS = hmcContentService.listByContentIdList(idList);
        return PojoUtils.map(hmcContentDOS, HMCContentDTO.class);
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

    @Override
    public Boolean contentRank(ContentRankRequest request) {
        return hmcContentService.contentRank(request);
    }

    @Override
    public Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request) {
        return hmcContentService.listAppContentPageBySql(request);
    }

    @Override
    public Boolean updateReferStatus(ContentReferStatusRequest request) {
        return hmcContentService.updateReferStatus(request);
    }
}