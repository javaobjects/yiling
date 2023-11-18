package com.yiling.cms.content.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.QaApi;
import com.yiling.cms.content.dto.QaDTO;
import com.yiling.cms.content.dto.request.AddQaRequest;
import com.yiling.cms.content.dto.request.QueryQAPageRequest;
import com.yiling.cms.content.dto.request.SwitchQAStatusRequest;
import com.yiling.cms.content.entity.QaDO;
import com.yiling.cms.content.service.QaService;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: fan.shen
 * @date: 2023/3/23
 */
@DubboService
public class QaApiImpl implements QaApi {

    @Autowired
    private QaService qaService;

    @Override
    public Long add(AddQaRequest request) {
        QaDO qaDO = PojoUtils.map(request, QaDO.class);
        qaService.save(qaDO);
        return qaDO.getId();
    }

    @Override
    public Integer getQaCountByContentId(Long contentId) {
        return qaService.getQaCountByContentId(contentId);
    }

    @Override
    public List<QaDTO> getQaListByContentId(Long contentId) {
        return qaService.getQaListByContentId(contentId);
    }

    @Override
    public Page<QaDTO> listPage(QueryQAPageRequest request) {
        return qaService.listPage(request);
    }

    @Override
    public Boolean switchShowStatus(SwitchQAStatusRequest request) {
        return qaService.switchShowStatus(request);
    }
}