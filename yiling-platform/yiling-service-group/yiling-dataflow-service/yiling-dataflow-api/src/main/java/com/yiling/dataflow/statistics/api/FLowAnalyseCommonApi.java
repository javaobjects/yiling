package com.yiling.dataflow.statistics.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.dto.FlowAnalyseEnterpriseDTO;
import com.yiling.dataflow.statistics.dto.FlowAnalyseGoodsDTO;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseEnterpriseRequest;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseGoodsRequest;

import java.util.List;

/**
 *
 */
public interface FLowAnalyseCommonApi {
  Page<FlowAnalyseGoodsDTO> getGoodsListByName(FlowAnalyseGoodsRequest request);
   Page<FlowAnalyseEnterpriseDTO> getEnterpriseListByName(FlowAnalyseEnterpriseRequest request);
}
