package com.yiling.cms.content.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.DraftDTO;
import com.yiling.cms.content.dto.request.AddContentRequest;
import com.yiling.cms.content.dto.request.DeleteDraftRequest;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.cms.content.dto.request.QueryContentPageRequest;
import com.yiling.cms.content.dto.request.UpdateContentRequest;
import com.yiling.cms.content.entity.ContentDO;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.base.request.BaseRequest;


/**
 * <p>
 * 内容 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
public interface ContentService extends BaseService<ContentDO> {

    /**
     * 添加内容
     *
     * @param addContentRequest
     */
    Long addContent(AddContentRequest addContentRequest);

    /**
     * 编辑内容
     *
     * @param updateContentRequest
     */
    void updateContent(UpdateContentRequest updateContentRequest);


    /**
     * 内容分页
     *
     * @param request
     * @return
     */
    Page<ContentDTO> listPage(QueryContentPageRequest request);

    /**
     * 查询单个内容
     * @param id
     * @return
     */
    ContentDTO getContentById(Long id);

    /**
     * app端内容列表
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPage(QueryAppContentPageRequest request);

    /**
     * 通过sql查询 app端内容列表
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
     * 更新阅读量
     * @param id
     */
    Integer updatePv(Long id);

    /**
     * 更新指定业务线阅读量
     * @param id
     */
    Integer updateLinePv(Long id, LineEnum lineEnum, AppContentDetailDTO appContentDetailDTO);

    /**
     * 更新点赞数
     * @param id
     * @param likeCount
     */
    Boolean updateLikeCount(Long id, Long likeCount);

    /**
     * 删除草稿
     * @param request
     */
    void deleteDraft(DeleteDraftRequest request);

    /**
     * 草稿列表
     * @param request
     * @return
     */
    List<DraftDTO> queryDraftList(BaseRequest request);

    /**
     * 批量获取
     * @param idList
     * @return
     */
    List<ContentDTO> getContentInfoByIdList(List<Long> idList);

    /**
     * 根据title查询
     * @param title
     * @return
     */
    List<ContentDTO> getByTitle(String title);

    // /**
    //  * 患者端内容列表
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
