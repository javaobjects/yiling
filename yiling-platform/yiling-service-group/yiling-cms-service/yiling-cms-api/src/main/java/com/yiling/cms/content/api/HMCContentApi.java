package com.yiling.cms.content.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.bo.ContentBO;
import com.yiling.cms.content.dto.*;
import com.yiling.cms.content.dto.request.*;
import com.yiling.framework.common.base.request.BaseRequest;

import java.util.List;

/**
 * HMC 内容管理
 * @author: fan.shen
 * @date: 2022/3/23
 */
public interface HMCContentApi {

    /**
     * 添加内容
     * @param addContentRequest
     */
     void addContent(AddHmcContentRequest addContentRequest);

    /**
     * 编辑内容
     * @param updateContentRequest
     */
    void updateContent(UpdateContentRequest updateContentRequest);

    /**
     * 内容分页
     * @param request
     * @return
     */
    Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request);

    /**
     * 单个内容
     * @param id
     * @return
     */
    ContentDTO getContentById(Long id);

    /**
     * 多个内容
     * @param idList
     * @return
     */
    List<HMCContentDTO> getContentByContentIdList(List<Long> idList);

    /**
     * 删除草稿
     * @param request
     */
    void deleteDraft(DeleteDraftRequest request);

    /**
     *草稿箱列表
     * @return
     */
    List<DraftDTO> queryDraftList(BaseRequest request);

    /**
     * 获取用户对文章点赞状态
     * @param currentUserId
     * @param id
     * @return
     */
    Integer getLikeStatus(Long currentUserId, Long id);

    /**
     * 点赞文章
     * @param request
     */
    Long likeContent(LikeContentRequest request);

    /**
     * 内容排序
     * @param request
     * @return
     */
    Boolean contentRank(ContentRankRequest request);

    /**
     * 分页查询内容
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request);

    /**
     * 修改引用状态
     * @param request
     * @return
     */
    Boolean updateReferStatus(ContentReferStatusRequest request);
}
