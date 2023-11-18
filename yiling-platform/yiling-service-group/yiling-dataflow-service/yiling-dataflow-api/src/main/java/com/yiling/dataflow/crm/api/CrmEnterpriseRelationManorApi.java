package com.yiling.dataflow.crm.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmRelationManorBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.dto.request.RemoveManorRelationRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateManorRelationRequest;

/**
 * 辖区企业关系api
 */
public interface CrmEnterpriseRelationManorApi {
    /**
     * 根据请求获取辖区机构信息
     *
     * @param request
     * @return
     */
    Page<CrmEnterpriseRelationManorDTO> pageListByManorId(QueryCrmEnterpriseRelationManorPageRequest request);

    /**
     * 保存辖区机构信息
     *
     * @param request
     * @return
     */
    Long saveOrUpdate(SaveOrUpdateManorRelationRequest request);

    /**
     * 批量更新
     * @param request
     * @return
     */
    Boolean updateBatch(List<SaveOrUpdateManorRelationRequest> request);

    /**
     * 删除辖区机构信息
     *
     * @param request
     * @return
     */
    int removeById(RemoveManorRelationRequest request);

    boolean checkDuplicate(Long crmEnId,Long categoryId, Long id);

    /**
     * 根据机构id查询
     * @param crmEnterpriseId
     * @return
     */
    List<CrmRelationManorBO> queryList(Long crmEnterpriseId);

    Page<CrmEnterpriseRelationManorDTO> pageExportList(QueryCrmManorPageRequest request);

    CrmEnterpriseRelationManorDTO getById(Long id);
}
