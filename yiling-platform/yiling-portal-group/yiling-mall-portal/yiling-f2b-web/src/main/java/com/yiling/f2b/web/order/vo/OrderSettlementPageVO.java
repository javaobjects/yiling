package com.yiling.f2b.web.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.f2b.web.enterprise.vo.DeliveryAddressVO;

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

    @ApiModelProperty("收货地址信息")
    private List<DeliveryAddressVO> deliveryAddressList;

    @ApiModelProperty("订单配送商列表")
    private List<OrderDistributorVO> orderDistributorList;

    @ApiModelProperty("是否显示企业账号信息")
    private Boolean showEasAccountInfoFlag;

    @ApiModelProperty("商务负责人列表")
    private List<ContactorVO> contactorList;

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

    @Data
    public static class ContactorVO {

        /**
         * ID
         */
        @ApiModelProperty("ID")
        private Long id;

        /**
         * 姓名
         */
        @ApiModelProperty("姓名")
        private String name;

        /**
         * 联系方式
         */
        @ApiModelProperty("联系方式")
        private String mobile;

        /**
         * 部门ID
         */
        @ApiModelProperty("部门Id")
        private Long  departmentId;

        /**
         * 部门名称
         */
        @ApiModelProperty("部门名称")
        private String departmentName;

        /**
         * 是否默认选中
         */
        @ApiModelProperty("是否默认选中")
        private Boolean selected;
    }

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
