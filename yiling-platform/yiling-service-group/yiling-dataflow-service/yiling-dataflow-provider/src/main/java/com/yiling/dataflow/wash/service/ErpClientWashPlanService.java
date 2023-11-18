package com.yiling.dataflow.wash.service;

import java.util.List;

import com.yiling.dataflow.wash.dto.ErpClientWashPlanDTO;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateErpClientWashPlanRequest;
import com.yiling.dataflow.wash.entity.ErpClientWashPlanDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * erp对接企业清洗计划表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-30
 */
public interface ErpClientWashPlanService extends BaseService<ErpClientWashPlanDO> {

    boolean generate(List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList);
    boolean updateByFmwcId(List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList);
    List<ErpClientWashPlanDTO> findListByFmwcId(Long fmwcId);
    boolean updateById(SaveOrUpdateErpClientWashPlanRequest request);
}
