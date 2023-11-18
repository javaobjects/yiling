package com.yiling.sjms.agency.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.AgencyUnLockFormDTO;
import com.yiling.sjms.agency.dto.AgencyUnLockRelationShipFormDTO;
import com.yiling.sjms.agency.dto.request.DeleteAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.EnterpriseUnLockApproveRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyUnlockFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * @author: shixing.sun
 * @date: 2023/2/22
 */
public interface AgencyUnLockFormApi {
    /**
     * 保存拓展信息修改
     * @param request
     */
    Long save(SaveAgencyUnlockFormRequest request);

    /**
     * 根据id删除
     * @param request
     */

    boolean deleteById(RemoveAgencyFormRequest request);

    /**
     * 分页查询 拓展信息
     * @param request
     * @return
     */
    Page<AgencyUnLockFormDTO> pageList(QueryAgencyFormPageRequest request);

    AgencyUnLockFormDTO queryById(Long id);

    Boolean submit(SubmitFormBaseRequest request);

    Boolean approved(EnterpriseUnLockApproveRequest request);

    List<Long> queryRelationListByAgencyFormId(Long id);

    void updateArchiveStatusById(UpdateAgencyLockArchiveRequest request);

    List<AgencyUnLockRelationShipFormDTO>  queryListByAgencyFormId(Long id);
}
