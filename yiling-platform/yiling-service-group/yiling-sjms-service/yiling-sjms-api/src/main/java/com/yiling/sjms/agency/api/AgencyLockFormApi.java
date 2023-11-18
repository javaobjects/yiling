package com.yiling.sjms.agency.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.request.DeleteAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockDetailPageRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockFormPageRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * @author: gxl
 * @date: 2023/2/22
 */
public interface AgencyLockFormApi {
    /**
     * 保存拓展信息修改
     * @param request
     * @return
     */
    Long save(SaveAgencyLockFormRequest request);

    /**
     * 分页查询 拓展信息
     * @param request
     * @return
     */
    Page<AgencyLockFormDTO> queryPage(QueryAgencyLockFormPageRequest request);

    /**
     * 根据id查询企业拓展信息修改表单
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
     *
     * @param request
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
     * 根据id更新归档状态
     *
     * @param request
     */
    void updateArchiveStatusById(UpdateAgencyLockArchiveRequest request);

    /**
     * 机构锁定表单提交审核
     *
     * @param request
     * @return
     */
    Boolean submitAgencyLockForm(SubmitFormBaseRequest request);

}
