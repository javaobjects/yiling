package com.yiling.order.order.service;

import com.yiling.order.order.bo.CombinationBuyNumberBO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.entity.OrderPromotionActivityDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单与促销活动关联表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-02-16
 */
public interface OrderPromotionActivityService extends BaseService<OrderPromotionActivityDO> {

    /**
     *  通过订单ID查询赠品活动信息
      * @param orderId 订单Id
     * @param activityTypeEnum 满赠活动类型
     * @return
     */
   public List<OrderPromotionActivityDTO> listByOrderId(Long orderId, PromotionActivityTypeEnum activityTypeEnum);

    /**
     * 通过订单ID查询赠品活动信息
     * @param orderId 订单ID
     * @return
     */
   public List<OrderPromotionActivityDTO> listByOrderId(Long orderId);

    /**
     * 根据类型获取订单信息
     * @param activityType
     * @return
     */
   List<OrderPromotionActivityDTO> listByActivityType(Integer activityType);


  /**
  * 查询组合包活动购买数量
  * @param buyerEid 企业Eid
  * @param activityId 活动Id
  * @return
  */
  CombinationBuyNumberBO sumCombinationActivityNumber(Long buyerEid, Long activityId);

 /**
 * 查询活动组合包总数量
 * @param activityIds
 * @return
 */
  Map<Long,Long> sumBatchCombinationActivityNumber(List<Long> activityIds);

}
