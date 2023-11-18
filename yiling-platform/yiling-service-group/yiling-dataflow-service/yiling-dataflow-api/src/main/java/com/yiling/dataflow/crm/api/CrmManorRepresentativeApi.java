package com.yiling.dataflow.crm.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorRepresentativePageRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmManorRepresentativeRequest;

import java.util.List;

public interface CrmManorRepresentativeApi {
    Page<CrmManorRepresentativeDTO> pageList(QueryCrmManorRepresentativePageRequest request);

    CrmManorRepresentativeDTO getByManorId(Long manorId);

    Long saveOrUpdate(SaveCrmManorRepresentativeRequest request);
}
