package com.yiling.cms.content.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.cms.content.dao.ContentLikeMapper;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.request.LikeContentRequest;
import com.yiling.cms.content.entity.ContentLikeDO;
import com.yiling.cms.content.entity.HmcContentDO;
import com.yiling.cms.content.enums.ContentLikeEnum;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.cms.content.service.ContentLikeService;
import com.yiling.cms.content.service.ContentService;
import com.yiling.cms.content.service.HmcContentService;
import com.yiling.cms.content.service.IHPatientContentService;
import com.yiling.framework.common.base.BaseServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 内容点赞表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-10-24
 */
@Service
@Slf4j
public class ContentLikeServiceImpl extends BaseServiceImpl<ContentLikeMapper, ContentLikeDO> implements ContentLikeService {

    @Autowired
    private ContentService contentService;

    @Autowired
    private HmcContentService hmcContentService;

    @Autowired
    private IHPatientContentService ihPatientContentService;

    @Override
    public Integer getLikeStatus(Long currentUserId, Long id) {
        LambdaQueryWrapper<ContentLikeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentLikeDO::getUserId, currentUserId);
        wrapper.eq(ContentLikeDO::getContentId, id);
        ContentLikeDO one = this.getOne(wrapper);
        if (Objects.nonNull(one) && one.getLikeFlag().equals(ContentLikeEnum.LIKE.getCode())) {
            return ContentLikeEnum.LIKE.getCode();
        }
        return ContentLikeEnum.UN_LIKE.getCode();
    }

    @Override
    @Transactional
    public Long likeContent(LikeContentRequest request) {
        Long likeCount = 0L;
        Long contentId = 0L;
        // 1、HMC点赞
        if (LineEnum.HMC.getCode().equals(request.getLineType())) {
            HmcContentDO hmcContentDO = hmcContentService.getById(request.getId());
            contentId = hmcContentDO.getContentId();
        }
        // 2、IH点赞
        if (LineEnum.IH_PATIENT.getCode().equals(request.getLineType())) {
            //IHPatientContentDO ihPatientContentDO = ihPatientContentService.getById(request.getCmsId());
            contentId = request.getId();
            // 暂时注释下面的逻辑
            // ihPatientContentService.updateLikeCount(request.getCmsId(), likeCount);
        }
        ContentDTO contentDTO = contentService.getContentById(contentId);
        likeCount = contentDTO.getLikeCount();
        if (request.getLikeFlag().equals(ContentLikeEnum.LIKE.getCode())) {
            likeCount += 1;
        } else {
            likeCount -= 1;
        }
        if (likeCount <= 0) {
            likeCount = 0L;
        }
        contentDTO.setLikeCount(likeCount);
        contentService.updateLikeCount(contentId, likeCount);

        // 2、创建、更新点赞记录
        LambdaQueryWrapper<ContentLikeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentLikeDO::getUserId, request.getOpUserId());
        wrapper.eq(ContentLikeDO::getContentId, request.getId());

        ContentLikeDO one = this.getOne(wrapper);
        if (Objects.nonNull(one)) {
            one.setLikeFlag(request.getLikeFlag());
            this.updateById(one);
        } else {
            ContentLikeDO likeDO = new ContentLikeDO();
            likeDO.setContentId(request.getId());
            likeDO.setUserId(request.getOpUserId());
            likeDO.setLikeFlag(request.getLikeFlag());
            this.save(likeDO);
        }
        return likeCount;
    }
}
