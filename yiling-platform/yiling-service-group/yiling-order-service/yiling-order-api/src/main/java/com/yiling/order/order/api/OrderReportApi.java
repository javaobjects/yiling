package com.yiling.order.order.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.bo.BuyerOrderSumBO;
import com.yiling.order.order.dto.ContacterOrderSumDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderFullDTO;
import com.yiling.order.order.dto.OrderSumDTO;
import com.yiling.order.order.dto.request.GoodDetailSumQueryPageRequest;
import com.yiling.order.order.dto.request.OrderSumQueryPageRequest;
import com.yiling.order.order.dto.request.QueryBuyerReceiveOrderDetailPageRequest;
import com.yiling.order.order.dto.request.QueryBuyerReceiveOrderPageRequest;
import com.yiling.order.order.dto.request.ReceiveOrderSumQueryRequest;

/** 订单统计API
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.api
 * @date: 2021/9/30
 */
public interface OrderReportApi {

    /**
     * 根据配送商商品ID查询已收货订单信息
     * @param distributorGoodsId 商品ID
     * @return
     */
    List<OrderDTO> selectReceiveOrderFullInfoByDistributorGoodsId(Long distributorGoodsId);

    /**
     * 通过以岭商品Id查询订单信息
     * @param goodId 以岭商品ID
     * @param buyerEidList 买家ID(买家ID为必填)
     * @return
     */
    List<OrderDTO> selectReceiveOrderFullInfoByGoodsId(Long goodId,List<Long> buyerEidList);

    /**
     * 通过商品Id查询订单信息（分页）,type 1表示根据以岭商品ID查询，2表示为根据配送商商品ID查询
     * @param goodDetailPageRequest
     * @return
     */
    Page<OrderDTO> pageReceiveOrderFullInfoByGoodsId(GoodDetailSumQueryPageRequest goodDetailPageRequest);

    /**
     * 通过企业ID获取卖家已收货订单信息
     * @param buyerReceiveOrderPageRequest 企业IDs(企业信息为必填)
     * @return
     */
    Page<OrderFullDTO> selectBuyerReceiveOrdersByEids(QueryBuyerReceiveOrderPageRequest buyerReceiveOrderPageRequest);

    /**
     * 统计已收货下单统计金额
     * @param queryRequest 下单人信息(下单人信息必填)
     * @return
     */
    OrderSumDTO sumReceiveOrderReportByCreateUserId(ReceiveOrderSumQueryRequest queryRequest);

    /**
     * 根据商务联系人统计，订单金额，以及收货数量
     * @param contacterIdList 商务联系人信息（商务联系人信息必填）
     * @return
     */
    List<ContacterOrderSumDTO> sumReceiveOrderReportByContacterId(List<Long> contacterIdList);

    /**
     * 统计已收货根据商务联系人总计统计金额
     * @param queryRequest 商务联系人信息（商务联系人信息必填）
     * @return
     */
    OrderSumDTO  sumReceiveOrderReportByContacterId(ReceiveOrderSumQueryRequest queryRequest);

    /**
     * 查询下单人的已收货订单信息
     * @param queryRequest 下单人信息(下单人信息为必填)
     * @return
     */
    List<OrderDTO> selectReceiveOrderListByCreateUserId(ReceiveOrderSumQueryRequest queryRequest);

    /**
     * 查询下单人的已收货订单信息分页
     * @param queryRequest 下单人信息(下单人信息为必填)
     * @return
     */
    Page<OrderDTO> selectReceiveOrderPageByCreateUserId(OrderSumQueryPageRequest queryRequest);

    /**
     * 查询商务联系人的已收货订单信息分页
     * @param queryRequest 商务联系人信息（商务联系人必填）
     * @return
     */
    Page<OrderDTO> selectReceiveOrderPageByContacterId(OrderSumQueryPageRequest queryRequest);

    /**
     * 查询下单人的订单信息
     * @param queryRequest 商务联系人信息
     * @return
     */
    List<OrderDTO> selectOrderListByContacterId(ReceiveOrderSumQueryRequest queryRequest);

    /**
     * 根据订单ID查询订单基本信息(包括订单明细信息)
     * @param orderId 订单Id
     * @return
     */
    OrderFullDTO selectOrderFullInfoByOrderId(Long orderId);

    /**
     * 按照商品维度，统计买家已收货商品信息
     * @param pageRequest 买家信息参数(买家信息为必填)
     * @return
     */
    BuyerOrderSumBO selectReceiveGoodsReportByBuyerListInfo(QueryBuyerReceiveOrderDetailPageRequest pageRequest);

    /**
     * 通过买家企业ID统计计已收货订单金额
     * @param buyerEid 企业ID
     * @return
     */
    OrderSumDTO sumReceiveOrderReportByBuyerEid(Long buyerEid);

}
