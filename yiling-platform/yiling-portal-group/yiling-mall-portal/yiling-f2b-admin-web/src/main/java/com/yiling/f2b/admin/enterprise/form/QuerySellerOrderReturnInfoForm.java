package com.yiling.f2b.admin.enterprise.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySellerOrderReturnInfoForm extends QueryPageListForm {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号", required = true)
    private String orderNo;
    /**
     * 退货单号
     */
    @ApiModelProperty(value = "退货单号", required = true)
    private String orderReturnNo;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式", required = true)
    private Long eid;
    /**
     * 采购商名称
     */
    @ApiModelProperty(value = "采购商名称", required = true)
    private String buyerEname;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endTime;

    /**
     * 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
     */
    @ApiModelProperty(value = "1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单", required = true)
    private Integer returnType;

    /**
     * 订单状态 1-待审核 2-审核通过 3-审核驳回
     */
    @ApiModelProperty(value = "订单状态 1-待审核 2-审核通过 3-审核驳回", required = true)
    private Integer returnStatus;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
     */
    @ApiModelProperty("部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门")
    private Integer departmentType;

}
