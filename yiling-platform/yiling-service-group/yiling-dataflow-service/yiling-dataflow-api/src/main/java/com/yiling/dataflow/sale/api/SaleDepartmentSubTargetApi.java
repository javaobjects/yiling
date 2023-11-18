package com.yiling.dataflow.sale.api;

import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.dto.request.SaveBathSaleDepartmentSubTargetRequest;

import java.util.List;

public interface SaleDepartmentSubTargetApi {

    List<SaleDepartmentSubTargetDTO> listByParam(QuerySaleDepartmentSubTargetRequest request);

    boolean saveBatch(SaveBathSaleDepartmentSubTargetRequest request);

    boolean updateBatch(SaveBathSaleDepartmentSubTargetRequest request);
}
