package com.yiling.cms.content.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.IHPatientContentDTO;
import com.yiling.cms.content.dto.IHPatientContentPageDTO;
import com.yiling.cms.content.dto.IHPatientHomeContentPageDTO;
import com.yiling.cms.content.dto.request.AddIHPatientContentRequest;
import com.yiling.cms.content.dto.request.QueryIHPatientContentPageRequest;
import com.yiling.cms.content.dto.request.UpdateContentCategoryRankRequest;
import com.yiling.cms.content.dto.request.UpdateIHPatientContentRequest;

/**
 * IHPatient内容管理
 *
 * @author: fan.shen
 * @date: 2022/3/23
 */
public interface IHPatientContentApi {

    /**
     * 批量查询
     *
     * @param contentIdList
     * @return
     */
    List<IHPatientContentDTO> getContentByContentIdList(List<Long> contentIdList);

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
     * 根据id查询
     *
     * @param id
     * @return
     */
    IHPatientContentDTO getIhPatientContentById(Long id);

    /**
     * 根据内容id批量查询
     *
     * @param contentIds
     * @param moduleId
     * @param categoryId
     * @return
     */
    List<IHPatientContentDTO> getIHPatientContentByContentIds(List<Long> contentIds);

    /**
     * 栏目排序
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
