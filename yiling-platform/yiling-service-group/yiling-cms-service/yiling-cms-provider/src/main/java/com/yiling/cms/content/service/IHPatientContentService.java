package com.yiling.cms.content.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.IHPatientContentPageDTO;
import com.yiling.cms.content.dto.IHPatientHomeContentPageDTO;
import com.yiling.cms.content.dto.request.AddIHPatientContentRequest;
import com.yiling.cms.content.dto.request.QueryIHPatientContentPageRequest;
import com.yiling.cms.content.dto.request.UpdateContentCategoryRankRequest;
import com.yiling.cms.content.dto.request.UpdateIHPatientContentRequest;
import com.yiling.cms.content.entity.IHPatientContentDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * IH患者端内容表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-11
 */
public interface IHPatientContentService extends BaseService<IHPatientContentDO> {

    /**
     * 根据contentID批量查询
     *
     * @param contentIdList
     * @return
     */
    List<IHPatientContentDO> listByContentIdList(List<Long> contentIdList);

    /**
     * 添加内容
     *
     * @param request
     */
    void addIHPatientContent(AddIHPatientContentRequest request);

    /**
     * 修改内容
     *
     * @param request
     */
    void updateIHPatientContent(UpdateIHPatientContentRequest request);

    /**
     * 内容列表
     *
     * @param request
     * @return
     */
    Page<IHPatientContentPageDTO> listContentPageBySql(QueryIHPatientContentPageRequest request);

    /**
     * 更新点赞数量
     *
     * @param cmsId
     * @param likeCount
     */
    Boolean updateLikeCount(Long cmsId, Long likeCount);

    /**
     * 更改阅读量
     *
     * @param id
     */
    void updateView(Long id);

    /**
     * 根据内容id批量查询
     *
     * @param contentIds
     * @param moduleId
     * @param categoryId
     * @return
     */
    List<IHPatientContentDO> getIHPatientContentByContentIds(List<Long> contentIds);

    /**
     * 更改排序
     *
     * @param request
     * @return
     */
    Boolean updateContentCategoryRank(UpdateContentCategoryRankRequest request);

    /**
     * 患者首页文章列表
     *
     * @param request
     * @return
     */
    Page<IHPatientHomeContentPageDTO> homeListContentPageBySql(QueryIHPatientContentPageRequest request);

    /**
     * 获取文章详情
     *
     * @param id
     * @param categoryId
     * @param moduleId
     * @return
     */
    ContentDTO getPatientContent(Long id, Long categoryId, Long moduleId);
}
