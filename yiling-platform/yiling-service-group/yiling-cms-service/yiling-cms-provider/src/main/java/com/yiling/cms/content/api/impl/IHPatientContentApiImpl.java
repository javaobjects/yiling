package com.yiling.cms.content.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.IHPatientContentApi;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.IHPatientContentDTO;
import com.yiling.cms.content.dto.IHPatientContentPageDTO;
import com.yiling.cms.content.dto.IHPatientHomeContentPageDTO;
import com.yiling.cms.content.dto.request.AddIHPatientContentRequest;
import com.yiling.cms.content.dto.request.QueryIHPatientContentPageRequest;
import com.yiling.cms.content.dto.request.UpdateContentCategoryRankRequest;
import com.yiling.cms.content.dto.request.UpdateIHPatientContentRequest;
import com.yiling.cms.content.entity.IHPatientContentDO;
import com.yiling.cms.content.service.IHPatientContentService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * IHDoc
 *
 * @author: fan.shen
 * @date: 2022/3/23
 */
@DubboService
public class IHPatientContentApiImpl implements IHPatientContentApi {

    @Autowired
    private IHPatientContentService ihPatientContentService;

    @Override
    public List<IHPatientContentDTO> getContentByContentIdList(List<Long> contentIdList) {
        List<IHPatientContentDO> ihPatientContentDOList = ihPatientContentService.listByContentIdList(contentIdList);
        return PojoUtils.map(ihPatientContentDOList, IHPatientContentDTO.class);
    }

    @Override
    public void addIHPatientContent(AddIHPatientContentRequest request) {
        ihPatientContentService.addIHPatientContent(request);
    }

    @Override
    public void updateIHPatientContent(UpdateIHPatientContentRequest request) {
        ihPatientContentService.updateIHPatientContent(request);
    }

    @Override
    public Page<IHPatientContentPageDTO> listContentPageBySql(QueryIHPatientContentPageRequest request) {
        return ihPatientContentService.listContentPageBySql(request);
    }

    @Override
    public IHPatientContentDTO getIhPatientContentById(Long id) {
        return PojoUtils.map(ihPatientContentService.getById(id), IHPatientContentDTO.class);
    }

    @Override
    public List<IHPatientContentDTO> getIHPatientContentByContentIds(List<Long> contentIds) {
        return PojoUtils.map(ihPatientContentService.getIHPatientContentByContentIds(contentIds), IHPatientContentDTO.class);
    }

    @Override
    public Boolean updateContentCategoryRank(UpdateContentCategoryRankRequest request) {
        return ihPatientContentService.updateContentCategoryRank(request);
    }

    @Override
    public Page<IHPatientHomeContentPageDTO> homeListContentPageBySql(QueryIHPatientContentPageRequest request) {
        return ihPatientContentService.homeListContentPageBySql(request);
    }

    @Override
    public ContentDTO getPatientContent(Long id, Long categoryId, Long moduleId) {
        return ihPatientContentService.getPatientContent(id, categoryId, moduleId);
    }
}