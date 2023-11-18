package com.yiling.order.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.bo.BuyerOrderDetailSumBO;
import com.yiling.order.order.bo.BuyerOrderSumBO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.BatchQueryUserBuyNumberRequest;
import com.yiling.order.order.dto.request.QueryBuyerOrderDetailPageRequest;
import com.yiling.order.order.dto.request.QueryPromotionNumberRequest;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.order.order.entity.OrderDetailDO;

/**
 * <p>
 * 订单明细 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Repository
public interface OrderDetailMapper extends BaseMapper<OrderDetailDO> {

    int updateReturnQuantity(OrderDetailDO orderDetailDO);

    /**
     * 根据商务联系人，以及企业ID查询订单明细信息
     * @param orderDetailQueryPageRequest 查询条件
     * @return
     */
    Page<BuyerOrderDetailSumBO>  selectOrderDetailReportListByBuyerEidList(Page<BuyerOrderDetailSumBO> page, @Param("orderDetailQueryPageRequest") QueryBuyerOrderDetailPageRequest orderDetailQueryPageRequest) ;

    /**
     * 根据买家信息，进行订单统计
     * @param orderDetailQueryPageRequest
     * @return
     */
    BuyerOrderSumBO selectBuyerSumReportByBuyerEidList(@Param("orderDetailQueryPageRequest") QueryBuyerOrderDetailPageRequest orderDetailQueryPageRequest);


    /**
     * @param contacterId   商务联系人ID
     * @param orderStatusList 订单状态
     * @return
     */
    Integer getRecevieGoodsOrderContacterId(@Param("contacterId") Long contacterId,@Param("orderStatusList") List<Integer> orderStatusList);


    /**
     * @param createUserId   创建人ID
     * @param orderStatusList 订单状态
     * @return
     */
    Integer getRecevieGoodsOrderByCreateUserId(@Param("createUserId") Long createUserId,@Param("orderStatusList") List<Integer> orderStatusList);


    /**
     * 查询已使用的促销活动商品数量
     * @param promotionNumberRequest
     * @return
     */
    Integer getPromotionNumberByDistributorGoodsId(@Param("promotionNumberRequest") QueryPromotionNumberRequest promotionNumberRequest);


    /**
     * 根据用户查询条件查询用户购买数量
     * @param request
     * @return
     */
    Long getUserBuyNumber(@Param("request") QueryUserBuyNumberRequest request);


    /**
     * 批量查询用户购买数量
     * @param request
     * @return
     */
    List<OrderDetailDTO> getBatchUserBuyNumber(@Param("request") BatchQueryUserBuyNumberRequest request);
}
