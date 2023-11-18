package com.yiling.cms.content.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.bo.ContentBO;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.DraftDTO;
import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.ContentDTO;

/**
 * @author: gxl
 * @date: 2022/3/23
 */
public interface ContentApi {

    /**
     * 添加内容
     * @param addContentRequest
     */
     void addContent(AddContentRequest addContentRequest);

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
    Page<ContentDTO> listPage(QueryContentPageRequest request);

    /**
     * 根据title模糊查询
     * @param title
     * @return
     */
    List<ContentDTO> getByTitle(String title);

    /**
     * 单个内容
     * @param id
     * @return
     */
    ContentDTO getContentById(Long id);

    /**
     * 批量获取
     * @param idList
     * @return
     */
    List<ContentDTO> getContentInfoByIdList(List<Long> idList);

    /**
     * app端内容列表
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPage(QueryAppContentPageRequest request);

    /**
     * app端内容列表
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request);

    /**
     * app端内容详情
     * @param id
     * @return
     */
    AppContentDetailDTO getContentDetail(Long id, LineEnum lineEnum);

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

    // /**
    //  * 患者端、医生端内容列表
    //  * @param request
    //  * @return
    //  */
    // Page<ContentBO> clientContentPage(QueryContentPageRequest request);

    // /**
    //  * 内容排序
    //  * @param request
    //  * @return
    //  */
    // Boolean contentRank(ContentRankRequest request);
    //
    // /**
    //  * 内容置顶
    //  * @param request
    //  * @return
    //  */
    // Boolean contentTop(ContentRankRequest request);
}
