package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 团购列表
 */
@Data
public class GbFormListBO {

    /**
     * 所属流程
     */
    private String bizName;

    /**
     * 状态
     */
    private String statusName;

    /**
     * 团购取消编码
     */
    private String srcGbNo;

    /**
     * 团购取消原因
     */
    private String srcGbReason;

    /**
     * 团购提报编码
     */
    private String gbNo;

    /**
     * 省区
     */
    private String provinceName;

    /**
     * 事业部
     */
    private String orgName;

    /**
     * 销量计入人工号
     */
    private String sellerEmpId;

    /**
     * 销量计入人名称
     */
    private String sellerEmpName;

    /**
     * 销量计入销量计入人区办人名称
     */
    private String sellerDeptName;

    /**
     * 销量计入人部门
     */
    private String sellerYxDeptName;

    /**
     * 团购负责人工号
     */
    private String managerEmpId;

    /**
     * 团购负责人姓名
     */
    private String managerEmpName;

    /**
     * 团购负责人部门
     */
    private String managerYxDeptName;

    /**
     *团购单位
     */
    private String customerName;

    /**
     *团购出库终端
     */
    private String termainalCompanyName;

    /**
     *团购出货终端CRM编码
     */
    private String termainalCompanyCode;

    /**
     *团购出库终端省区名称
     */
    private String termainalCompanyProvince;

    /**
     *团购出库终端市区名称
     */
    private String termainalCompanyCity;

    /**
     *团购出库商业
     */
    private String businessCompanyName;

    /**
     *团购出货终端CRM编码
     */
    private String businessCompanyCode;

    /**
     *团购出库商业省区名称
     */
    private String businessCompanyProvince;

    /**
     *团购出库商业市区名称
     */
    private String businessCompanyCity;

    /**
     *团购月份
     */
    private String month;

    /**
     * 团购成功回款日期
     */
    private String paymentTime;

    /**
     * 申请团购政策-返利（金额,元）
     */
    private BigDecimal rebateAmount;

    /**
     * 团购区域
     */
    private String regionName;

    /**
     * 团购性质
     */
    private String gbName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 团购数量（盒）
     */
    private Integer quantityBox;

    /**
     * 团购数量（件）
     */
    private String quantity;

    /**
     * 实际团购单价
     */
    private String finalPrice;

    /**
     * 实际团购金额
     */
    private String finalAmount;

    /**
     * 产品核算价
     */
    private String price;

    /**
     * 团购核算总额
     */
    private String amount;

    /**
     * 证据类型
     */
    private String evidences;

    /**
     * 团购属性调整
     */
    private String gbReviewName;

    /**
     * 是否地级市及以下政府机构
     */
    private String gbCityBelowName;

    /**
     * 是否复核: 1-否 2-是
     */
    private String reviewStatus;

    /**
     * 复核意见
     */
    private String reviewReply;


    /**
     * 复核时间
     */
    private Date reviewTime;

    /**
     * 流向月份
     */
    private String flowMonthDay;

    /**
     * 财务审批意见
     */
    private String approveReply;

    /**
     * 团购费用申请原因
     */
    private String feeApplicationReply;
}
