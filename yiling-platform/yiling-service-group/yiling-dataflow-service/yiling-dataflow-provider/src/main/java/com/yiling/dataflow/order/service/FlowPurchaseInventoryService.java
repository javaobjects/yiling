package com.yiling.dataflow.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryTotalPoQuantityRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向商品库存信息 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2022-05-26
 */
@Service
public interface FlowPurchaseInventoryService extends BaseService<FlowPurchaseInventoryDO> {

    /**
     * 根据商业eid、商品内码、渠道来源查询流向商品库存
     *
     * @param eid 商业id
     * @param goodsInSn 商品内码
     * @param source 采购来源：1-大运河, 2-京东
     * @return
     */
    FlowPurchaseInventoryDO getByEidAndGoodsInSnAndSource(Long eid, String goodsInSn, Integer source);

    /**
     * 根据商业eid、商品id、批次号查询流向商品库存
     *
     * @param request
     * @return
     */
    List<FlowPurchaseInventoryDO> getListByEidAndYlGoodsIdAndGoodsInSn(QueryFlowPurchaseInventorySettlementRequest request);

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
    List<FlowPurchaseInventoryDO> getListByEidAndYlGoodsIdAndGoodsInSnAndSource(QueryFlowPurchaseInventorySettlementRequest request);

    /**
     * 保存采购流向库存
     *
     * @return
     */
    Boolean saveOrUpdateTotalPoQuantity(SaveFlowPurchaseInventoryTotalPoQuantityRequest request);

    /**
     * 根据eid、goodsInSn查询分页
     *
     * @param request
     * @return
     */
    Page<FlowPurchaseInventoryDO> pageByEidAndYlGoodsId(QueryFlowPurchaseInventoryListPageRequest request);

    /**
     * 根据商业id、商业商品内码查询库存
     *
     * @param eid
     * @param goodsInSn
     * @return
     */
    List<FlowPurchaseInventoryDO> getByEidAndGoodsInSn(Long eid, String goodsInSn);
}
