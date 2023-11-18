package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchDetailWashDTO;
import com.yiling.dataflow.wash.dto.FlowPurchaseWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashListRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
public interface FlowGoodsBatchDetailWashService extends BaseService<FlowGoodsBatchDetailWashDO> {

    Page<FlowGoodsBatchDetailWashDO> listPage(QueryFlowGoodsBatchDetailWashPageRequest request);

    void batchInsert(List<FlowGoodsBatchDetailWashDO> flowGoodsBatchDetailWashDOList);

    List<FlowGoodsBatchDetailWashDO> getByFmwtId(Long fmwtId);

    /**
     * 根据所属年月，以及商品信息获取List
     * @param request
     * @return
     */
    List<FlowGoodsBatchDetailWashDTO> getByYearMonth(QueryFlowGoodsBatchDetailWashListRequest request);

    Page<FlowGoodsBatchDetailWashDTO> getPageByYearMonth(QueryFlowGoodsBatchDetailWashPageRequest request);

    /**
     * 修改商品对照信息，以及对照状态
     * @param request
     */
    void updateCrmGoodsInfo(UpdateCrmGoodsInfoRequest request);

    Integer goodsUnMappingStatusCount(Long fmwtId);

}
