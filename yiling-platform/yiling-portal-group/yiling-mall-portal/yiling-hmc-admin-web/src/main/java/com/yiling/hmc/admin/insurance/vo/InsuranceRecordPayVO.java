package com.yiling.hmc.admin.insurance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * C端参保记录表
 * </p>
 *
 * @author gxl
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=true)
public class InsuranceRecordPayVO extends BaseVO {


    @ApiModelProperty(hidden = true)
    private Long eid;
    /**
     * 保险id
     */
    @ApiModelProperty(value = "保险id")
    private Long insuranceId;

    @ApiModelProperty(value = "保单id")
    private Long insuranceRecordId;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;


    /**
     * 保单号
     */
    @ApiModelProperty(value = "保司保单号")
    private String policyNo;


    /**
     * 平台单号
     */
    @ApiModelProperty(value = "销售交易单号")
    private String orderNo;



    /**
     * 定额方案类型 1-季度，2-年
     */
    @ApiModelProperty(value = "定额方案类型 1-季度，2-年")
    private Integer billType;


    /**
     * 被保人名称
     */
    @ApiModelProperty(value = "被保人名称")
    private String issueName;

    /**
     * 被保人联系方式
     */
    @ApiModelProperty(value = "被保人联系方式")
    private String issuePhone;

    /**
     * 投保人联系方式
     */
    @ApiModelProperty(value = "投保人联系方式")
    private String holderPhone;
    /**
     * 投保人姓名
     */
    @ApiModelProperty(value = "投保人名称")
    private String holderName;
    /**
     * 保险详情id
     */
    @ApiModelProperty(value = "保险详情id")
    private Integer policyInfoId;

    /**
     * 销售人员id
     */
    @ApiModelProperty(value = "销售人员id 来源终端类型： 等于0代表线上 大于0代表线下")
    private Long sellerUserId;

    @ApiModelProperty(value = "销售员姓名")
    private String sellerUserName;

    /**
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    @ApiModelProperty(value = "保单状态 1-进行中，2-已退保，3-已终止，4-已失效")
    private Integer policyStatus;


    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 兑付次数
     */
    @ApiModelProperty(value = "兑付次数")
    private Integer cashTimes;

    /**
     * 累计支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal  amount;

    @ApiModelProperty(value = "保司")
    private String companyName;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "交费时间")
    private Date payTime;


    @ApiModelProperty(value = "保单来源终端")
    private String terminalName;

    /**
     * 销售人员所属eid
     */
    private Long sellerEid;
}
