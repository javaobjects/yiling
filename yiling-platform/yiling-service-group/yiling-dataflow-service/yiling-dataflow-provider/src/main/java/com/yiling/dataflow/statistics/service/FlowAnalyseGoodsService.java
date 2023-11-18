package com.yiling.dataflow.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.dto.FlowAnalyseGoodsDTO;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseGoodsRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseGoodsDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 商品信息配置表 服务类
 * </p>
 *
 * @author handy
 * @date 2023-01-05
 */
public interface FlowAnalyseGoodsService extends BaseService<FlowAnalyseGoodsDO> {

    Page<FlowAnalyseGoodsDO> getGoodsListByName(FlowAnalyseGoodsRequest request);

    List<Long> getSpecificationIdList();

    List<FlowAnalyseGoodsDTO> getGoodsNameBySpecificationIds(List<String> specificationIds);
}
