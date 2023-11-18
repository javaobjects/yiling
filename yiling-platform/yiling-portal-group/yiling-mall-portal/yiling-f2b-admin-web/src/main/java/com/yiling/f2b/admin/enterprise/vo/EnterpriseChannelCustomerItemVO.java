package com.yiling.f2b.admin.enterprise.vo;

import java.util.Date;
import java.util.List;

import com.yiling.f2b.admin.payment.vo.PaymentMethodVO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 渠道商列表项 VO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseChannelCustomerItemVO extends BaseVO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户ID
     */
    @ApiModelProperty("客户ID")
    private Long customerEid;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 企业地址
     */
    @ApiModelProperty("企业地址")
    private String address;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty("企业状态")
    private String status;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 客户类型
     */
    @ApiModelProperty("客户类型")
    private String customerType;

    /**
     * 渠道名称
     */
    @ApiModelProperty("渠道名称")
    private String channelName;

    /**
     * 渠道ID
     */
    @ApiModelProperty("渠道ID")
    private Long channelId;

    /**
     * 采购关系个数
     */
    @ApiModelProperty("采购关系个数")
    private Integer purchaseRelationNum;

    /**
     * 客户支付方式列表
     */
    @ApiModelProperty("客户支付方式列表")
    private List<PaymentMethodVO> customerPaymentMethods;

    /**
     * 商务联系人个数
     */
    @ApiModelProperty("商务联系人个数")
    private Integer customerContactNum;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 支付方式拼接
     */
    @ApiModelProperty("支付方式拼接(适应导出模板)")
    private String paymentMethodContact;

    /**
     * 分组ID
     */
    @ApiModelProperty("客户分组ID")
    private Long customerGroupId;

}
