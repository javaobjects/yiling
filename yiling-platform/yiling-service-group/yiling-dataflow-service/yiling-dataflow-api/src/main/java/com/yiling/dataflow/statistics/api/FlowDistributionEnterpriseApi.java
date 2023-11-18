package com.yiling.dataflow.statistics.api;

import java.util.List;

import com.yiling.dataflow.statistics.dto.FlowDistributionEnterpriseDTO;

/**
 * @author: houjie.sun
 * @date: 2023/2/7
 */
public interface FlowDistributionEnterpriseApi {

    List<FlowDistributionEnterpriseDTO> getListByCodeList(List<String> codeList);

    List<FlowDistributionEnterpriseDTO> getListByEidList(List<Long> eidList);

    Integer getCountByEidList(List<Long> eidList);

    List<FlowDistributionEnterpriseDTO> getListByCrmIdListAndEnameLevel(List<Long> crmIdList, String enameLevel);

    List<FlowDistributionEnterpriseDTO> listByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList);

}
