package com.yiling.dataflow.crm.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmRelationManorBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateManorRelationRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationManorDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 辖区机构品类关系 服务类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-09
 */
public interface CrmEnterpriseRelationManorService extends BaseService<CrmEnterpriseRelationManorDO> {

    List<CrmEnterpriseRelationManorDTO> getByManorId(Long id);

    int batchDeleteWithIds(List<Long> idList, Long opUserId,String message);

    Page<CrmEnterpriseRelationManorDTO> pageListByManorId(QueryCrmEnterpriseRelationManorPageRequest request);

    boolean checkDuplicate(Long crmEnId, Long categoryId,Long id);

    /**
     * 根据机构id查询
     * @param crmEnterpriseId
     * @return
     */
    List<CrmRelationManorBO> queryList(Long crmEnterpriseId);

    Page<CrmEnterpriseRelationManorDTO> pageExportList(QueryCrmManorPageRequest request);

    /**
     * 批量更新
     * @param request
     * @return
     */
    Boolean updateBatch(List<SaveOrUpdateManorRelationRequest> request);
}
