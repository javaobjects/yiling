package com.yiling.user.procrelation.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.procrelation.api.PopProcTemplateApi;
import com.yiling.user.procrelation.dto.PopProcTemplateDTO;
import com.yiling.user.procrelation.dto.request.QueryProcTemplatePageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcTemplateRequest;
import com.yiling.user.procrelation.entity.PopProcTemplateDO;
import com.yiling.user.procrelation.service.PopProcTemplateService;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@DubboService
public class PopProcTemplateApiImpl implements PopProcTemplateApi {

    @Autowired
    PopProcTemplateService templateService;

    @Override
    public Long saveProcTemplate(SaveProcTemplateRequest request) {
        return templateService.saveProcTemplate(request);
    }

    @Override
    public Page<PopProcTemplateDTO> queryPageList(QueryProcTemplatePageRequest request) {
        Page<PopProcTemplateDO> page = templateService.queryPageList(request);
        return PojoUtils.map(page, PopProcTemplateDTO.class);
    }

    @Override
    public PopProcTemplateDTO queryTemplateById(Long templateId) {
        PopProcTemplateDO templateDO = templateService.getById(templateId);
        return PojoUtils.map(templateDO, PopProcTemplateDTO.class);
    }

    @Override
    public PopProcTemplateDTO queryTemplateByNumber(String templateNumber) {
        PopProcTemplateDO templateDO = templateService.queryTemplateByNumber(templateNumber);
        return PojoUtils.map(templateDO,PopProcTemplateDTO.class);
    }

    @Override
    public Boolean deleteById(Long templateId,Long opUser) {
        return templateService.deleteById(templateId,opUser);
    }


}
