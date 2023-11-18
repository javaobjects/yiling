package com.yiling.sjms.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.gb.api.GbWorkflowProcessorApi;
import com.yiling.sjms.gb.service.GbOrgManagerService;
import com.yiling.sjms.gb.service.GbProvinceManagerService;
import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;
import com.yiling.user.esb.dto.request.QueryProvinceManagerRequest;

/**
 * 团购工作流处理人 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/11/28
 */
@DubboService
public class GbWorkflowProcessorApiImpl implements GbWorkflowProcessorApi {

    @Autowired
    GbProvinceManagerService gbProvinceManagerService;
    @Autowired
    GbOrgManagerService gbOrgManagerService;

    @Override
    public SimpleEsbEmployeeInfoBO getByProvinceName(QueryProvinceManagerRequest request) {
        return gbProvinceManagerService.getByProvinceName(request);
    }

    @Override
    public SimpleEsbEmployeeInfoBO getByOrgId(Long orgId) {
        return gbOrgManagerService.getByOrgId(orgId);
    }

    @Override
    public List<SimpleEsbEmployeeInfoBO> listByOrgIds(List<Long> orgIds) {
        return gbOrgManagerService.listByOrgIds(orgIds);
    }
}
