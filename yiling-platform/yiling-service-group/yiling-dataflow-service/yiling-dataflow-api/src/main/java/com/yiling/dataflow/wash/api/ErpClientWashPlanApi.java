package com.yiling.dataflow.wash.api;

import java.util.List;

import com.yiling.dataflow.wash.dto.ErpClientWashPlanDTO;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateErpClientWashPlanRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/5/30
 */
public interface ErpClientWashPlanApi {
    boolean generate(List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList);
    boolean updateByFmwcId(List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList);
    List<ErpClientWashPlanDTO> findListByFmwcId(Long fmwcId);
    boolean updateById(SaveOrUpdateErpClientWashPlanRequest request);
}
