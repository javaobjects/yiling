package com.yiling.sjms.agency.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.AgencyUnLockFormDTO;
import com.yiling.sjms.agency.dto.request.DeleteAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.EnterpriseUnLockApproveRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyUnlockFormRequest;
import com.yiling.sjms.agency.entity.AgencyUnlockFormDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * <p>
 * 机构解锁表单 服务类
 * </p>
 *
 * @author handy
 * @date 2023-02-22
 */
public interface AgencyUnlockFormService extends BaseService<AgencyUnlockFormDO> {

    Long save(SaveAgencyUnlockFormRequest request);

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
    boolean deleteById(RemoveAgencyFormRequest request);

    Page<AgencyUnLockFormDTO> pageList(QueryAgencyFormPageRequest request);

    Boolean submit(SubmitFormBaseRequest request);

    Boolean approved(EnterpriseUnLockApproveRequest request);
}
