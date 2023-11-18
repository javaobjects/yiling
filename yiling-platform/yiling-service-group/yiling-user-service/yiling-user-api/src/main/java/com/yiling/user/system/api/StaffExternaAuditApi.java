package com.yiling.user.system.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.dto.StaffExternaAuditDTO;
import com.yiling.user.system.dto.request.AuditStaffExternaInfoRequest;
import com.yiling.user.system.dto.request.CreateStaffExternaAuditRequest;
import com.yiling.user.system.dto.request.QueryStaffExternaAuditPageListRequest;

/**
 * 外部员工审核 API
 *
 * @author: xuan.zhou
 * @date: 2022/1/17
 */
public interface StaffExternaAuditApi {

    /**
     * 查询外部员工账户审核信息分页列表
     *
     * @param request
     * @return
     */
    Page<StaffExternaAuditDTO> pageList(QueryStaffExternaAuditPageListRequest request);

    /**
     * 根据ID获取审核信息
     *
     * @param id
     * @return
     */
    StaffExternaAuditDTO getById(Long id);

    /**
     * 审核个人信息
     *
     * @param request
     * @return
     */
    boolean audit(AuditStaffExternaInfoRequest request);

    /**
     * 获取用户最新的审核信息
     *
     * @param userId 用户ID
     * @return
     */
    StaffExternaAuditDTO getUserLatestAuditInfo(Long userId);

    /**
     * 创建审核信息
     *
     * @param request
     * @return
     */
    boolean create(CreateStaffExternaAuditRequest request);
}
