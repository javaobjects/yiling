package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.sales.assistant.app.deliveryAddress.vo.DeliveryAddressVO;
import com.yiling.sales.assistant.app.rebate.vo.OrderDistributorVO;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单结算页 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/23
 */
@Data
public class OrderSettlementPageVO {

    @ApiModelProperty("默认收货地址信息")
    private DeliveryAddressVO deliveryAddress;

    @ApiModelProperty("订单配送商列表")
    private List<OrderDistributorVO> orderDistributorList;

    @ApiModelProperty("是否显示企业账号信息")
    private Boolean showEasAccountInfoFlag;

    @ApiModelProperty("企业账号列表")
    private List<EasAccountVO> easAccountList;

    @ApiModelProperty("商品种数")
    private Long goodsSpeciesNum;

    @ApiModelProperty("商品件数")
    private Long goodsNum;

    @ApiModelProperty("商品总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("运费")
    private BigDecimal freightAmount;

    @ApiModelProperty("应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("客户ID")
    private Long customerEid;

    @ApiModelProperty("订单类型(1:POP订单,2:B2B订单)")
    private Integer orderType;


    @Data
    public static class EasAccountVO {

        @ApiModelProperty("企业名称")
        private String ename;

        @ApiModelProperty("账号")
        private String account;

        @ApiModelProperty("是否默认选中")
        private Boolean selected;
    }

    public static OrderSettlementPageVO empty() {
        OrderSettlementPageVO pageVO = new OrderSettlementPageVO();
        pageVO.setOrderDistributorList(ListUtil.empty());
        pageVO.setGoodsSpeciesNum(0L);
        pageVO.setGoodsNum(0L);
        pageVO.setTotalAmount(BigDecimal.ZERO);
        pageVO.setFreightAmount(BigDecimal.ZERO);
        pageVO.setPaymentAmount(BigDecimal.ZERO);
        return pageVO;
    }
}
