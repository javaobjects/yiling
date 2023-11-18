package com.yiling.f2b.web.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货单列表返回给前端的实体
 *
 * @author: yong.zhang
 * @date: 2022/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnVO extends BaseVO {

    @ApiModelProperty(value = "退货单id")
    private Long id;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "退货单号")
    private String returnNo;

    @ApiModelProperty(value = "退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台")
    private Integer returnSource;

    @ApiModelProperty(value = "买家企业id")
    private Long buyerEid;

    @ApiModelProperty(value = "买家名称")
    private String buyerEname;

    @ApiModelProperty(value = "卖家企业id")
    private Long sellerEid;

    @ApiModelProperty(value = "卖家名称")
    private String sellerEname;

    @ApiModelProperty(value = "配送商企业ID")
    private Long distributorEid;

    @ApiModelProperty(value = "配送商名称")
    private String distributorEname;

    @ApiModelProperty(value = "开票节点：1-开票前 2-开票后")
    private Integer invoiceStatus;

    @ApiModelProperty(value = "退货单类型：1-供应商退货单 2-破损退货单 3-采购退货单")
    private Integer returnType;

    @ApiModelProperty(value = "退货单状态：1-待审核 2-审核通过 3-审核驳回")
    private Integer returnStatus;

    @ApiModelProperty(value = "退货单审核人")
    private Long returnAuditUser;

    @ApiModelProperty(value = "退货单审核时间")
    private Date returnAuditTime;

    @ApiModelProperty(value = "驳回原因")
    private String failReason;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "现折金额")
    private BigDecimal cashDiscountAmount;

    @ApiModelProperty(value = "票折金额")
    private BigDecimal ticketDiscountAmount;

    @ApiModelProperty(value = "平台优惠劵折扣金额")
    private BigDecimal platformCouponDiscountAmount;

    @ApiModelProperty(value = "商家优惠劵折扣金额")
    private BigDecimal couponDiscountAmount;

    @ApiModelProperty(value = "商务联系人ID")
    private Long contacterId;

    @ApiModelProperty(value = "商务联系人姓名")
    private String contacterName;

    @ApiModelProperty(value = "ERP推送状态：1-未推送 2-推送成功 3-推送失败")
    private Integer erpPushStatus;

    @ApiModelProperty(value = "ERP推送时间")
    private Date erpPushTime;

    @ApiModelProperty(value = "ERP推送备注")
    private String erpPushRemark;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "退货单备注")
    private String remark;
}
