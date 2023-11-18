package com.yiling.dataflow.order.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowPurchaseInventoryBO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryListPageDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryLogListPageDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryLogListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequest;

/**
 * 流向商品库存信息 接口
 *
 * @author: houjie.sun
 * @date: 2022/5/26
 */
public interface FlowPurchaseInventoryApi {

    /**
     * 根据商业eid、商品内码查询流向商品库存
     *
     * @param eid 商业id
     * @param goodsInSn 商品内码
     * @param source 采购来源：1-大运河, 2-京东
     * @return
     */
    FlowPurchaseInventoryDTO getByEidAndGoodsInSnAndSource(Long eid, String goodsInSn, Integer source);

    /**
     * 根据商业eid、商品id、商品内码查询流向商品库存
     *
     * @param request
     * @return
     */
    List<FlowPurchaseInventoryBO> getListByEidAndYlGoodsIdAndGoodsInSn(QueryFlowPurchaseInventorySettlementRequest request);

    /**
     * 根据主键id更新库存数量
     *
     * @param request
     * @return
     */
    int updateQuantityById(UpdateFlowPurchaseInventoryQuantityRequest request);

    /**
     * 保存采购流向库存
     *
     * @return
     */
    Boolean saveOrUpdateFlowPurchaseInventory(SaveFlowPurchaseInventoryRequest request);

    /**
     * 根据商业eid、商品id、商品内码、来源查询流向商品库存
     *
     * @param request
     * @return
     */
    List<FlowPurchaseInventoryDTO> getListByEidAndYlGoodsIdAndGoodsInSnAndSource(QueryFlowPurchaseInventorySettlementRequest request);

    /**
     * 根据eid、goodsInSn查询分页
     *
     * @param request
     * @return
     */
    Page<FlowPurchaseInventoryListPageDTO> pageByEidAndYlGoodsId(QueryFlowPurchaseInventoryListPageRequest request);


    /**
     * 根据eid、goodsInSn查询分页
     *
     * @param request
     * @return
     */
    Page<FlowPurchaseInventoryLogListPageDTO> adjustmentLogPageLogByInventoryId(QueryFlowPurchaseInventoryLogListPageRequest request);

    /**
     * 根据采购库存id列表查询调整库存之和
     *
     * @param inventoryIdList
     * @return
     */
    Map<Long, BigDecimal> getSumAdjustmentQuantityByInventoryIdList(List<Long> inventoryIdList);
    
}
