package com.yiling.sjms.agency.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.agency.dto.AgencyRelationShipChangeFormDTO;
import com.yiling.sjms.agency.dto.request.EnterpriseUnLockApproveRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyRelationChangeFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyRelationShipChangeFormDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * <p>
 * 机构锁定三者关系变更表单 服务类
 * </p>
 *
 * @author handy
 * @date 2023-02-22
 */
public interface AgencyRelationShipChangeFormService extends BaseService<AgencyRelationShipChangeFormDO> {

    Long save(SaveAgencyRelationChangeFormRequest request);

    boolean deleteById(RemoveAgencyFormRequest request);

    Page<AgencyRelationShipChangeFormDTO> pageList(QueryAgencyFormPageRequest request);

    Boolean submit(SubmitFormBaseRequest request);

    Boolean approved(EnterpriseUnLockApproveRequest request);

    Boolean updateArchiveStatusById(UpdateAgencyLockArchiveRequest request);

    List<String> getNameByFormId(Long id);
}
