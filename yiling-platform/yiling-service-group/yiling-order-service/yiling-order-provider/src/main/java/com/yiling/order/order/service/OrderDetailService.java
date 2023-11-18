package com.yiling.order.order.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.bo.BuyerOrderSumBO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.request.BatchQueryUserBuyNumberRequest;
import com.yiling.order.order.dto.request.QueryBuyerOrderDetailPageRequest;
import com.yiling.order.order.dto.request.QueryPromotionNumberRequest;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.order.order.entity.OrderDetailDO;

/**
 * <p>
 * 订单明细 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
public interface OrderDetailService extends BaseService<OrderDetailDO> {
    /**
     * 根据orderId查询订单明细
     * @param orderIds
     * @return
     */
    List<OrderDetailDO> getOrderDetailByOrderIds(List<Long> orderIds);

    /**
     * 根据订单查询购买收货返货数量和种类
     * @param orderId
     * @return
     */
    OrderGoodsTypeAndNumberDTO getOrderGoodsTypeAndNumber(Long orderId);

    /**
     * 通过买家ID查询收货数量
     * @param ContacterId
     * @return
     */
    Integer getRecevieGoodsOrderContacterId(Long ContacterId,List<Integer> orderStatusList);

    /**
     * 获取明细信息
     * @param orderId
     * @return
     */
    List<OrderDetailDTO> getOrderDetailInfo(Long orderId);

    /**
     * 按照商品维度，统计买家商品信息
     * @param pageRequest 买家信息参数
     * @return
     */
    BuyerOrderSumBO selectGoodsReportByBuyerListInfo(QueryBuyerOrderDetailPageRequest pageRequest);


    /**
     * 查询促销活动商品系信
     * @param promotionNumberRequest
     * @return
     */
    Integer getPromotionNumberByDistributorGoodsId(QueryPromotionNumberRequest promotionNumberRequest);


    /*********************************************限购接口***************************************************/

    /**
     * 根据查询规则，批量查询商品已购买数量
     * @param request
     * @return
     */
    Map<Long,Long> getBatchUserBuyNumber(BatchQueryUserBuyNumberRequest request);

    /**
     * 根据条件查询商品已购买数量
     * @param queryUserBuyNumberRequest
     * @return
     */
    Long getUserBuyNumber(QueryUserBuyNumberRequest queryUserBuyNumberRequest);

    /**
     * 根据条件查询商品已购买数量
     * @param queryUserBuyNumberRequests
     * @return
     */
    Map<Long, Long> getUserBuyNumber(List<QueryUserBuyNumberRequest> queryUserBuyNumberRequests);

}
