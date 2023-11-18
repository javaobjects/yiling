package com.yiling.f2b.web.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配送商订单信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/23
 */
@Data
public class OrderDistributorVO {

    @ApiModelProperty("配送商企业ID")
    private Long distributorEid;

    @ApiModelProperty("配送商名称")
    private String distributorName;

    @ApiModelProperty("商品列表")
    private List<OrderGoodsVO> orderGoodsList;

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

    @ApiModelProperty("现折优惠金额")
    private BigDecimal cashDiscountAmount;

    @ApiModelProperty("购销合同文件列表")
    private List<FileInfoVO> contractFileList;

    @ApiModelProperty(value = "买家留言")
    private String buyerMessage;

    @ApiModelProperty("是否是以岭配送商")
    private Boolean yilingFlag;

    @ApiModelProperty("是否需要购销合同")
    private Boolean showContractFile;

    @ApiModelProperty("支付方式")
    private List<PaymentMethodVO> paymentMethodList;

    @Data
    public static class PaymentMethodVO {

        @ApiModelProperty("支付方式ID")
        private Long id;

        @ApiModelProperty("支付方式名称")
        private String name;

        @ApiModelProperty("是否可用")
        private Boolean enabled;

        @ApiModelProperty("是否选中")
        private Boolean selected;

        @ApiModelProperty("不可用原因")
        private String disabledReason;
    }
}
