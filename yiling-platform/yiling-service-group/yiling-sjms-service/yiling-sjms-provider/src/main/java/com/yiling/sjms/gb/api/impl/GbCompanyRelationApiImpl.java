package com.yiling.sjms.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbCompanyRelationApi;
import com.yiling.sjms.gb.dto.GbCompanyRelationDTO;
import com.yiling.sjms.gb.service.GbCompanyRelationService;

/**
 * 团购出库终端和商业关系
 *
 * @author: wei.wang
 * @date: 2023/03/07
 */
@DubboService
public class GbCompanyRelationApiImpl implements GbCompanyRelationApi {

    @Autowired
    private GbCompanyRelationService gbCompanyRelationService;

    @Override
    public List<GbCompanyRelationDTO> listByFormId(Long formId) {
        return PojoUtils.map(gbCompanyRelationService.listByFormId(formId),GbCompanyRelationDTO.class);
    }

    @Override
    public List<GbCompanyRelationDTO> listByFormIds(List<Long> formIds) {
        return PojoUtils.map(gbCompanyRelationService.listByFormIds(formIds),GbCompanyRelationDTO.class);

    }
}
