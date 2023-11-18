package com.yiling.user.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.dto.request.AuditStaffExternaInfoRequest;
import com.yiling.user.system.dto.request.CreateStaffExternaAuditRequest;
import com.yiling.user.system.dto.request.QueryStaffExternaAuditPageListRequest;
import com.yiling.user.system.entity.StaffExternaAuditDO;

/**
 * <p>
 * 外部员工账户审核信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-17
 */
public interface StaffExternaAuditService extends BaseService<StaffExternaAuditDO> {

    /**
     * 查询外部员工账户审核信息分页列表
     *
     * @param request
     * @return
     */
    Page<StaffExternaAuditDO> pageList(QueryStaffExternaAuditPageListRequest request);

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
    StaffExternaAuditDO getUserLatestAuditInfo(Long userId);

    /**
     * 创建审核信息
     *
     * @param request
     * @return
     */
    boolean create(CreateStaffExternaAuditRequest request);
}
