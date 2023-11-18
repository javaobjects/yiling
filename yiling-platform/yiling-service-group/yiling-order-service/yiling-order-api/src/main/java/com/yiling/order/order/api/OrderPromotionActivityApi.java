package com.yiling.order.order.api;

import java.util.List;
import java.util.Map;

import com.yiling.order.order.bo.CombinationBuyNumberBO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

/**
 * 订单与促销活动关联表
 * @author:wei.wang
 * @date:2021/6/22
 */
public interface OrderPromotionActivityApi {

    /**
     *
     * @param orderIds
     * @return
     */
    List<OrderPromotionActivityDTO> listByOrderIds(List<Long> orderIds);


    OrderPromotionActivityDTO getOneByOrderIds(Long orderId );


    /** 根据活动Id查询活动参与促销活动信息
     * @param activityIds 活动Id
     * @param promotionActivityTypeEnums 促销活动类型
     * @return 参与促销活动的活动记录数据
     */
    List<OrderPromotionActivityDTO> listByActivityIds(List<Long> activityIds, PromotionActivityTypeEnum... promotionActivityTypeEnums);

     /**
     * 查询组合包活动数量
     * @param buyerEid 企业Id
     * @param activityId 组合包活动Id
     * @return
     */
    CombinationBuyNumberBO sumCombinationActivityNumber(Long buyerEid,Long activityId);


    /**
     * 批量查询组合包已购买数量
     * @param activityIds 活动Id
     * @return
     */
    Map<Long,Long> sumBatchCombinationActivityNumber(List<Long> activityIds);



}
