package com.yiling.cms.content.service;

import com.yiling.cms.content.dto.request.LikeContentRequest;
import com.yiling.cms.content.entity.ContentLikeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 内容点赞表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-10-24
 */
public interface ContentLikeService extends BaseService<ContentLikeDO> {

    /**
     * 获取用户点赞状态
     * @param currentUserId
     * @param id
     * @return
     */
    Integer getLikeStatus(Long currentUserId, Long id);

    /**
     * 文章点赞
     * @param request
     */
    Long likeContent(LikeContentRequest request);
}
