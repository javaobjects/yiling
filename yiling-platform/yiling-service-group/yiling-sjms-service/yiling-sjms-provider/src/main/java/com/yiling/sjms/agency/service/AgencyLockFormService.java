package com.yiling.sjms.agency.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.request.ApproveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.ApproveToAgLockRequest;
import com.yiling.sjms.agency.dto.request.DeleteAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockDetailPageRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockFormPageRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyLockFormDO;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * <p>
 * 机构锁定信息表 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-02-22
 */
public interface AgencyLockFormService extends BaseService<AgencyLockFormDO> {

    /**
     * 保存拓展信息修改
     * @param request
     */
    Long save(SaveAgencyLockFormRequest request);


    /**
     * 分页查询拓展信息
     * @param request
     * @return
     */
    Page<AgencyLockFormDTO> queryPage(QueryAgencyLockFormPageRequest request);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    AgencyLockFormDTO getAgencyLockForm(Long id);

    /**
     * 根据id删除
     * @param request
     */
    void deleteById(DeleteAgencyLockFormRequest request);

    /**
     * 保存或更新机构锁定
     * @param request
     * @return
     */
    Long saveAgencyLock(SaveAgencyLockRequest request);

    /**
     * 查询锁定机构申请的明细
     *
     * @param request
     * @return
     */
    Page<AgencyLockFormDTO> queryAgencyLockDetailPage(QueryAgencyLockDetailPageRequest request);

    /**
     * 机构扩展信息修改表单提交审核
     * @param request
     * @return
     */
    Boolean submit(SubmitFormBaseRequest request);

    /**
     * 审批通过 修改机构扩展信息
     * @param request
     * @return
     */
    Boolean approveToChange(ApproveAgencyLockFormRequest request);

    /**
     * 根据id更新归档状态
     *
     * @param request
     */
    void updateArchiveStatusById(UpdateAgencyLockArchiveRequest request);

    /**
     * 审批通过 机构锁定
     *
     * @param request
     * @return
     */
    Boolean approveToAgLock(ApproveToAgLockRequest request);

    /**
     * 机构锁定表单提交审核
     *
     * @param request
     * @return
     */
    Boolean submitAgencyLockForm(SubmitFormBaseRequest request);

}
