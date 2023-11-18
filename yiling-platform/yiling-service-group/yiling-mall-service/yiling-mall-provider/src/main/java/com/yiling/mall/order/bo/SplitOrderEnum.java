package com.yiling.mall.order.bo;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.mall.order.handler.AbstractOrderSplitHandler;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拆单枚举类型
 *
 * @author zhigang.guo
 * @date: 2022/4/27
 */
@Getter
@AllArgsConstructor
public enum SplitOrderEnum {


    NORMAL("正常订单", SplitOrderEnum.NORMAL_ORDER), PRESALE("预售订单", SplitOrderEnum.PRE_SALE_ORDER), SPECIAL("特价&秒杀订单", SplitOrderEnum.SPECIAL_ORDER), COMBINATION("组合订单", SplitOrderEnum.COMBINATION_ORDER);

    private String name;

    private String splitClassName;

    // 正常订单
    public static final String NORMAL_ORDER = "normalOrderSplitHandler";
    // 预售订单
    public static final String PRE_SALE_ORDER = "presaleOrderSplitHandler";
    // 特价&秒杀订单
    public static final String SPECIAL_ORDER = "specialOrderSplitHandler";
    // 组合订单
    public static final String COMBINATION_ORDER = "combinationOrderSplitHandler";


    /**
     * 获取拆单处理器
     *
     * @return
     */
    public AbstractOrderSplitHandler handlerInstance() {

        return (AbstractOrderSplitHandler) SpringUtils.getBean(this.getSplitClassName());
    }

    /**
     * 获取拆单类型
     * code
     * {@link com.yiling.order.order.enums.PromotionActivityTypeEnum}
     *
     * @return
     */
    public static SplitOrderEnum getByPromotionActivityCode(Integer code) {
        SplitOrderEnum splitOrderEnum;
        switch (PromotionActivityTypeEnum.getByCode(code)) {
            case SPECIAL:
            case LIMIT:
                splitOrderEnum = SplitOrderEnum.SPECIAL;
                break;
            case COMBINATION:
                splitOrderEnum = SplitOrderEnum.COMBINATION;
                break;
            case PRESALE:
                splitOrderEnum = SplitOrderEnum.PRESALE;
                break;
            default:
                splitOrderEnum = SplitOrderEnum.NORMAL;

        }
        return splitOrderEnum;
    }


    public static SplitOrderEnum getByCode(String name) {
        for (SplitOrderEnum e : SplitOrderEnum.values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }


    /**
     * 过滤订单拆单数据
     *
     * @param request
     * @param cartDOList
     * @return
     */
    public static List<OrderSubmitRequest.DistributorOrderDTO> filterData(OrderSubmitRequest request, List<CartDO> cartDOList) {
        List<OrderSubmitRequest.DistributorOrderDTO> distributorOrderList = Lists.newArrayList();
        List<Long> cartList = cartDOList.stream().map(t -> t.getId()).collect(Collectors.toList());
        for (OrderSubmitRequest.DistributorOrderDTO orderDTO : request.getDistributorOrderList()) {
            List<Long> cartIdList = orderDTO.getCartIds().stream().filter(t -> cartList.contains(t)).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(cartIdList)) {
                continue;
            }
            OrderSubmitRequest.DistributorOrderDTO copyDistributorOrder = new OrderSubmitRequest.DistributorOrderDTO();
            copyDistributorOrder.setBuyerMessage(orderDTO.getBuyerMessage());
            copyDistributorOrder.setShopCustomerCouponId(orderDTO.getShopCustomerCouponId());
            copyDistributorOrder.setContractNumber(orderDTO.getContractNumber());
            copyDistributorOrder.setContractFileKeyList(orderDTO.getContractFileKeyList());
            copyDistributorOrder.setPaymentMethod(orderDTO.getPaymentMethod());
            copyDistributorOrder.setPaymentType(orderDTO.getPaymentType());
            copyDistributorOrder.setDistributorEid(orderDTO.getDistributorEid());
            copyDistributorOrder.setCartIds(cartIdList);
            distributorOrderList.add(copyDistributorOrder);
        }
        return distributorOrderList;
    }
}
