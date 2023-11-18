package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.entity.OrderAttachmentDO;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;

/**
 * <p>
 * 订单相关附件 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-07-12
 */
public interface OrderAttachmentService extends BaseService<OrderAttachmentDO> {

    /**
     * 批量保存订单附件数据
     *
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param attachmentTypeEnum 附件类型枚举
     * @param attachmentKeyList 附件KEY列表
     * @param opUserId 操作人ID
     * @return
     */
    boolean saveBatch(Long orderId, String orderNo, OrderAttachmentTypeEnum attachmentTypeEnum, List<String> attachmentKeyList, Long opUserId);

    /**
     * 获取订单附件列表
     *
     * @param orderId 订单ID
     * @param attachmentTypeEnum 附件类型枚举
     * @return
     */
    List<OrderAttachmentDO> listByOrderId(Long orderId, OrderAttachmentTypeEnum attachmentTypeEnum);

    /**
     * 删除附件
     * @param orderId
     * @param attachmentTypeEnum
     * @param opUserId
     * @return
     */
    Boolean deleteByOrderId(Long orderId,OrderAttachmentTypeEnum attachmentTypeEnum,Long opUserId);
}
