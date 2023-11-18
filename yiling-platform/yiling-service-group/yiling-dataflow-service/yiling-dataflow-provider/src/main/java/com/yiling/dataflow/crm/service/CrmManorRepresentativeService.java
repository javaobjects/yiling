package com.yiling.dataflow.crm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorRepresentativePageRequest;
import com.yiling.dataflow.crm.entity.CrmManorRepresentativeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 辖区代表 服务类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-12
 */
public interface CrmManorRepresentativeService extends BaseService<CrmManorRepresentativeDO> {

    Page<CrmManorRepresentativeDTO> pageList(QueryCrmManorRepresentativePageRequest request);

    CrmManorRepresentativeDTO getByManorId(Long manorId);

    void  batchDeleteWithManorId(Long manorId,Long opUserId,String message);
}
