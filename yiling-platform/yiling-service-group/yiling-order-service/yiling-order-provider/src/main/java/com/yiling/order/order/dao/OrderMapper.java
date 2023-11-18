package com.yiling.order.order.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.dto.OrderB2BPaymentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryReportCountDTO;
import com.yiling.order.order.dto.OrderEnterpriseDTO;
import com.yiling.order.order.dto.OrderExpectExportDTO;
import com.yiling.order.order.dto.OrderExportReportDetailDTO;
import com.yiling.order.order.dto.OrderMemberDiscountDTO;
import com.yiling.order.order.dto.OrderMemberDiscountDTO;
import com.yiling.order.order.dto.OrderNumberDTO;
import com.yiling.order.order.dto.OrderTypeGoodsQuantityDTO;
import com.yiling.order.order.dto.request.GoodDetailSumQueryPageRequest;
import com.yiling.order.order.dto.request.OrderB2BPaymentRequest;
import com.yiling.order.order.dto.request.QueryAssistantOrderFirstRequest;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.dto.request.QueryDiscountOrderBuyerEidPagRequest;
import com.yiling.order.order.dto.request.QueryDiscountOrderBuyerEidPagRequest;
import com.yiling.order.order.dto.request.QueryOrderDeliveryReportRequest;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.dto.request.QueryOrderExportReportPageRequest;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.entity.OrderDO;

/**
 * <p>
 * 订单 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Repository
public interface OrderMapper extends BaseMapper<OrderDO> {
    /**
     * 预订单列表导出接口
     * @param request
     * @return
     */
    List<OrderExpectExportDTO> orderExpectExport(@Param("request")QueryOrderExpectPageRequest request);

    /**
     * 通过配送商商品ID查询已发货的订单
     * @param distributorGoodsId 配送商商品ID
     * @return
     */
    List<OrderDO> selectReceiveOrderByDistributorGoodId(@Param("distributorGoodsId") Long distributorGoodsId);

    /**
     * 通过以岭商品ID查询已发货订单信息
     * @param goodId 以岭商品ID
     * @param buyerEidList 买家ID
     * @return
     */
    List<OrderDO> selectReceiveOrderByGoodId(@Param("goodId") Long goodId,@Param("buyerEidList") List<Long> buyerEidList);

    /**
     * 通过以岭商品ID查询已发货订单信息
     * @param page 分页参数
     * @param pageRequest 买家ID,type,1-以岭商品ID,2-配送商商品iD
     * @return
     */
    Page<OrderDO> pageReceiveOrderByGoodId(Page<OrderDO> page, @Param("pageRequest") GoodDetailSumQueryPageRequest pageRequest);

    /**
     * B2B移动端订单账期列表
     * @param page
     * @param request
     * @return
     */
    Page<OrderB2BPaymentDTO> getOrderB2BPaymentList(Page<OrderDO> page,@Param("request") OrderB2BPaymentRequest request);

    /**
     * 根据买家企业id统计累计订单金额
     * @param buyerEid
     * @return
     */
    BigDecimal getTotalAmountByBuyerEid(@Param("buyerEid") Long buyerEid);

    /**
     * 获取金额
     * @param request
     * @return
     */
    OrderNumberDTO getTotalAmount(@Param("request")QueryBackOrderInfoRequest request);
    /**
     *报表导出各省份付款金额
     * @param request
     * @return
     */
    List<OrderExportReportDetailDTO> getOrderPaymentReport(@Param("request")QueryOrderExportReportPageRequest request);


    /**
     *报表导出各省份发货减退货数量
     * @param request
     * @return
     */
    List<OrderExportReportDetailDTO> getOrderQuantityReport(@Param("request")QueryOrderExportReportPageRequest request);

    /**
     * (大屏)发货报表数据统计
     * @param request
     * @return
     */
    List<OrderDeliveryReportCountDTO>getOrderDeliveryReportCount(@Param("request") QueryOrderDeliveryReportRequest request);

    /**
     *
     * @param request
     * @return
     */
    List<Long> verificationReceiveB2BOrder(@Param("request") QueryAssistantOrderFirstRequest request);

    /**
     *
     * @param request
     * @return
     */
    OrderDO getAssistantReceiveFirstOrder(@Param("request") QueryAssistantOrderFirstRequest request);

    /**
     * 会员获取总折扣优惠金额
     * @param request
     * @return
     */
    BigDecimal getMemberOrderAllDiscountAmount(@Param("request") QueryDiscountOrderBuyerEidPagRequest request);

    /**
     * 获取会员统计省钱金额
     * @param request
     * @return
     */
    Page<OrderMemberDiscountDTO> getMemberOrderDiscountInfo(Page<OrderDO> page, @Param("request")QueryDiscountOrderBuyerEidPagRequest request);

    /**
     * 根据商品id获取5天内的发货
     * @param goodsIdList 商品id
     * @return
     */
    List<Long> getDeliveryFiveDayTips(@Param("goodsIdList") List<Long> goodsIdList);

    /**
     * 统计前天收货订单
     *
     * @param orderTypeList 订单来源
     */
    List<Long> countReceiveOrder(@Param("orderTypeList") List<Integer> orderTypeList,@Param("date") String date);

    /**
     * 企业订单信息查询
     * @param page
     * @param request
     * @return
     */
    Page<OrderEnterpriseDTO> getOrderEnterprisePage(Page<OrderDO> page, @Param("request") QueryOrderPageRequest request);

    /**
     * 获取B2B商家后台数量
     * @param request
     * @return
     */
    Long getB2BAdminNumber ( @Param("request") QueryOrderPageRequest request);
    /**
     * 根据订单类型统计商品销量
     * @param request
     * @return
     */
    List<OrderTypeGoodsQuantityDTO> getCountOrderTypeQuantity(@Param("request") QueryOrderPageRequest request);
}
