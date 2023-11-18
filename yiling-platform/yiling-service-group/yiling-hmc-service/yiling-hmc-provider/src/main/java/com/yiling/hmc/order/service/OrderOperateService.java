package com.yiling.hmc.order.service;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.entity.OrderOperateDO;
import com.yiling.hmc.order.enums.HmcOrderOperateTypeEnum;

/**
 * <p>
 * 订单操作表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-04-21
 */
public interface OrderOperateService extends BaseService<OrderOperateDO> {

    /**
     * 获取某订单的最新操作
     *
     * @param orderId 订单id
     * @return 操作记录
     */
    OrderOperateDO getLastOperate(Long orderId);

    /**
     * 根据订单id和类型查询订单操作信息
     *
     * @param orderId 订单id
     * @param operateTypeList 操作功能:1-自提/2-发货/3-退货/4-收货
     * @return 订单操作信息
     */
    List<OrderOperateDO> listByOrderIdAndTypeList(Long orderId, List<Integer> operateTypeList);

    /**
     * 订单操作新增
     *
     * @param orderId 订单id
     * @param hmcOrderOperateTypeEnum 操作功能:1-自提/2-发货/3-退货/4-收货
     * @param content 内容日志
     * @param opUserId 操作人
     * @param opTime 操作时间
     * @return 成功/失败
     */
    boolean saveOrderOperate(Long orderId, HmcOrderOperateTypeEnum hmcOrderOperateTypeEnum, String content, Long opUserId, Date opTime);

}
