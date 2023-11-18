package com.yiling.dataflow.statistics.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseEnterpriseRequest;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.entity.FlowDistributionEnterpriseDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 企业信息配置表 服务类
 * </p>
 *
 * @author handy
 * @date 2023-01-05
 */
public interface FlowDistributionEnterpriseService extends BaseService<FlowDistributionEnterpriseDO> {

    Page<FlowDistributionEnterpriseDO> getEnterpriseListByName(FlowAnalyseEnterpriseRequest request);

    List<Long> getEidByParmam(StockForecastInfoRequest request);

    List<Long> getEidList();

    List<FlowDistributionEnterpriseDO> getListByCodeList(List<String> codeList);

    List<FlowDistributionEnterpriseDO> getListByEidList(List<Long> eidList);

    Integer getCountByEidList(List<Long> eidList);

    List<FlowDistributionEnterpriseDO> getListByCrmIdListAndEnameLevel(List<Long> crmIdList, String enameLevel);

    List<FlowDistributionEnterpriseDO> listByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList);
}
