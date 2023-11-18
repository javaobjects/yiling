package com.yiling.sjms.agency.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.agency.dto.AgencyRelationShipChangeFormDTO;
import com.yiling.sjms.agency.dto.AgencyUnLockFormDTO;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyRelationChangeFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyUnlockFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * @author: gxl
 * @date: 2023/2/22
 */
public interface AgencyRelationShipChangeFormApi {
    /**
     * 保存拓展信息修改
     * @param request
     */
    Long save(SaveAgencyRelationChangeFormRequest request);

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
    Page<AgencyRelationShipChangeFormDTO> pageList(QueryAgencyFormPageRequest request);

    AgencyRelationShipChangeFormDTO queryById(Long id);

    Boolean submit(SubmitFormBaseRequest request);

    List<Long> queryRelationListByAgencyFormId(Long id);

    Boolean updateArchiveStatusById(UpdateAgencyLockArchiveRequest request);

    List<String> getNameByFormId(Long id);
}
