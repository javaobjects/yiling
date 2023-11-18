package com.yiling.admin.erp.enterprisecustomer.form;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 TycEnterpriseInfoForm
 * @描述
 * @创建时间 2022/1/13
 * @修改人 shichen
 * @修改时间 2022/1/13
 **/
@Data
public class TycEnterpriseInfoForm {
    /**
     * 天眼查企业id
     */
    @ApiModelProperty(value = "天眼查企业id")
    private Long id;

    /**
     * 人员规模
     */
    @ApiModelProperty(value = "人员规模")
    private String staffNumRange;

    /**
     * 经营开始时间
     */
    @ApiModelProperty(value = "经营开始时间")
    private Long fromTime;

    /**
     * 法人类型 1 人 2 公司
     */
    @ApiModelProperty(value = "法人类型 1 人 2 公司")
    private Integer type;

    /**
     * 股票名
     */
    @ApiModelProperty(value = "股票名")
    private String bondName;

    /**
     * 是否是小微企业 0不是 1是
     */
    @ApiModelProperty(value = "是否是小微企业 0不是 1是")
    private Integer isMicroEnt;

    /**
     * 股票曾用名
     */
    @ApiModelProperty(value = "股票曾用名")
    private String usedBondName;

    /**
     * 注册号
     */
    @ApiModelProperty(value = "注册号")
    private String regNumber;

    /**
     * 企业评分
     */
    @ApiModelProperty(value = "企业评分")
    private BigDecimal percentileScore;

    /**
     * 注册资本
     */
    @ApiModelProperty(value = "注册资本")
    private String regCapital;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 登记机关
     */
    @ApiModelProperty(value = "登记机关")
    private String regInstitute;

    /**
     * 注册地址
     */
    @ApiModelProperty(value = "注册地址")
    private String regLocation;

    /**
     * 行业
     */
    @ApiModelProperty(value = "行业")
    private String industry;

    /**
     * 核准时间
     */
    @ApiModelProperty(value = "核准时间")
    private Long approvedTime;

    /**
     * 参保人数
     */
    @ApiModelProperty(value = "参保人数")
    private Integer socialStaffNum;

    /**
     * 企业标签
     */
    @ApiModelProperty(value = "企业标签")
    private String tags;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String taxNumber;

    /**
     * 经营范围
     */
    @ApiModelProperty(value = "经营范围")
    private String businessScope;

    /**
     * 英文名
     */
    @ApiModelProperty(value = "英文名")
    private String property3;

    /**
     * 简称
     */
    @ApiModelProperty(value = "简称")
    private String alias;

    /**
     * 组织机构代码
     */
    @ApiModelProperty(value = "组织机构代码")
    private String orgNumber;

    /**
     * 企业状态
     */
    @ApiModelProperty(value = "企业状态")
    private String regStatus;

    /**
     * 成立日期
     */
    @ApiModelProperty(value = "成立日期")
    private Long estiblishTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTimes;

    /**
     * 股票类型
     */
    @ApiModelProperty(value = "股票类型")
    private String bondType;

    /**
     * 法人
     */
    @ApiModelProperty(value = "法人")
    private String legalPersonName;

    /**
     * 经营结束时间
     */
    @ApiModelProperty(value = "经营结束时间")
    private Long toTime;

    /**
     * 实收注册资金
     */
    @ApiModelProperty(value = "实收注册资金")
    private String actualCapital;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private String companyOrgType;

    /**
     * 省份简称
     */
    @ApiModelProperty(value = "省份简称")
    private String base;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    /**
     * 曾用名
     */
    @ApiModelProperty(value = "曾用名")
    private String historyNames;

    /**
     * 曾用名
     */
    @ApiModelProperty(value = "曾用名")
    private List<String> historyNameList;

    /**
     * 股票号
     */
    @ApiModelProperty(value = "股票号")
    private String bondNum;

    /**
     * 注册资本币种 人民币 美元 欧元 等
     */
    @ApiModelProperty(value = "注册资本币种 人民币 美元 欧元 等")
    private String regCapitalCurrency;

    /**
     * 实收注册资本币种 人民币 美元 欧元 等
     */
    @ApiModelProperty(value = "实收注册资本币种 人民币 美元 欧元 等")
    private String actualCapitalCurrency;

    /**
     * 吊销日期
     */
    @ApiModelProperty(value = "吊销日期")
    private Long revokeDate;

    /**
     * 吊销原因
     */
    @ApiModelProperty(value = "吊销原因")
    private String revokeReason;

    /**
     * 注销日期
     */
    @ApiModelProperty(value = "注销日期")
    private Long cancelDate;

    /**
     * 注销原因
     */
    @ApiModelProperty(value = "注销原因")
    private String cancelReason;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String district;
}
