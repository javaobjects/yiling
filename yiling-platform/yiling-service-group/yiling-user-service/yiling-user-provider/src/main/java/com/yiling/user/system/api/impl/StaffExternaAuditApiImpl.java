package com.yiling.user.system.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.StaffExternaAuditApi;
import com.yiling.user.system.dto.StaffExternaAuditDTO;
import com.yiling.user.system.dto.request.AuditStaffExternaInfoRequest;
import com.yiling.user.system.dto.request.CreateStaffExternaAuditRequest;
import com.yiling.user.system.dto.request.QueryStaffExternaAuditPageListRequest;
import com.yiling.user.system.service.StaffExternaAuditService;

/**
 * 外部员工审核 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/1/17
 */
@DubboService
public class StaffExternaAuditApiImpl implements StaffExternaAuditApi {

    @Autowired
    private StaffExternaAuditService staffExternaAuditService;

    @Override
    public Page<StaffExternaAuditDTO> pageList(QueryStaffExternaAuditPageListRequest request) {
        return PojoUtils.map(staffExternaAuditService.pageList(request), StaffExternaAuditDTO.class);
    }

    @Override
    public StaffExternaAuditDTO getById(Long id) {
        return PojoUtils.map(staffExternaAuditService.getById(id), StaffExternaAuditDTO.class);
    }

    @Override
    public boolean audit(AuditStaffExternaInfoRequest request) {
        return staffExternaAuditService.audit(request);
    }

    @Override
    public StaffExternaAuditDTO getUserLatestAuditInfo(Long userId) {
        return PojoUtils.map(staffExternaAuditService.getUserLatestAuditInfo(userId), StaffExternaAuditDTO.class);
    }

    @Override
    public boolean create(CreateStaffExternaAuditRequest request) {
        return staffExternaAuditService.create(request);
    }
}
