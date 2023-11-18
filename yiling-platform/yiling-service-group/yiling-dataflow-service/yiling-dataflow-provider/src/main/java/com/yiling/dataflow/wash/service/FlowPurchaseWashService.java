package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowPurchaseWashDTO;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashListRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmOrganizationInfoRequest;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
public interface FlowPurchaseWashService extends BaseService<FlowPurchaseWashDO> {

    /**
     * 分页查询采购流向清洗明细
     * @param request
     * @return
     */
    Page<FlowPurchaseWashDO> listPage(QueryFlowPurchaseWashPageRequest request);

    /**
     * 批量插入
     * @param flowSaleWashDOList
     */
    void batchInsert(List<FlowPurchaseWashDO> flowPurchaseWashDOList);

    /**
     * 根据任务清洗id获取List
     * @param fmwtId
     * @return
     */
    List<FlowPurchaseWashDO> getByFmwtId(Long fmwtId);

    /**
     * 根据所属年月，以及商品信息获取List
     * @param request
     * @return
     */
    List<FlowPurchaseWashDTO> getByYearMonth(QueryFlowPurchaseWashListRequest request);

    Page<FlowPurchaseWashDTO> getPageByYearMonth(QueryFlowPurchaseWashPageRequest request);

    /**
     * 修改商品对照信息，以及对照状态
     * @param request
     */
    void updateCrmGoodsInfo(UpdateCrmGoodsInfoRequest request);

    /**
     * 修改供应商对照信息
     * @param request
     */
    void updateCrmOrganizationInfo(UpdateCrmOrganizationInfoRequest request);

    /**
     * 查询未对照供应商数量
     * @param request
     */
    Integer organizationUnMappingStatusCount(Long fmwtId);

    Integer goodsUnMappingStatusCount(Long fmwtId);
}
