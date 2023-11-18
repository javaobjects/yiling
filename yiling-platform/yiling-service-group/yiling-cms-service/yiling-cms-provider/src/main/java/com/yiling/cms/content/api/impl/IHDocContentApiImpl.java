package com.yiling.cms.content.api.impl;

import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.content.entity.IHDoctorContentDO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.IHDocContentApi;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.IHDocContentDTO;
import com.yiling.cms.content.service.IHDocContentService;
import com.yiling.framework.common.util.PojoUtils;

import java.util.List;

/**
 * IHDoc
 *
 * @author: fan.shen
 * @date: 2022/3/23
 */
@DubboService
public class IHDocContentApiImpl implements IHDocContentApi {

    @Autowired
    private IHDocContentService ihDocContentService;

    @Override
    public void addContent(AddIHDocContentRequest addContentRequest) {
        ihDocContentService.addContent(addContentRequest);
    }

    @Override
    public Page<IHDocContentDTO> listPage(QueryIHDocContentPageRequest request) {
        return ihDocContentService.listPage(request);
    }

    @Override
    public Boolean contentRank(ContentRankRequest request) {
        return ihDocContentService.contentRank(request);
    }

    @Override
    public Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request) {
        return ihDocContentService.listAppContentPageBySql(request);
    }

    @Override
    public IHDocContentDTO getIhDocContentById(Long id) {
        return PojoUtils.map(ihDocContentService.getById(id), IHDocContentDTO.class);
    }

    @Override
    public List<IHDocContentDTO> getContentByContentIdList(List<Long> contentIdList) {
        List<IHDoctorContentDO> ihDoctorContentDOS = ihDocContentService.listByContentIdList(contentIdList);
        return PojoUtils.map(ihDoctorContentDOS, IHDocContentDTO.class);
    }

    @Override
    public Boolean updateReferStatus(ContentReferStatusRequest request) {
        return ihDocContentService.updateReferStatus(request);
    }

    @Override
    public Boolean updateIHDocContentAuth(UpdateContentAuthRequest request) {
        return ihDocContentService.updateIHDocContentAuth(request);
    }
}