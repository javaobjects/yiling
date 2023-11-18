package com.yiling.basic.tianyancha.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 TycEnterpriseInfoDO
 * @描述
 * @创建时间 2022/2/28
 * @修改人 shichen
 * @修改时间 2022/2/28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tyc_enterprise_info")
public class TycEnterpriseInfoDO extends BaseDO {
    /**
     * 人员规模
     */
    private String staffNumRange;

    /**
     * 经营开始时间
     */
    private Date fromTime;

    /**
     * 法人类型 1 人 2 公司
     */
    private Integer type;

    /**
     * 股票名
     */
    private String bondName;

    /**
     * 是否是小微企业 0不是 1是
     */
    private Integer isMicroEnt;

    /**
     * 股票曾用名
     */
    private String usedBondName;

    /**
     * 注册号
     */
    private String regNumber;

    /**
     * 企业评分
     */
    private Integer percentileScore;

    /**
     * 注册资本
     */
    private String regCapital;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 登记机关
     */
    private String regInstitute;

    /**
     * 注册地址
     */
    private String regLocation;

    /**
     * 行业
     */
    private String industry;

    /**
     * 核准时间
     */
    private Date approvedTime;

    /**
     * 参保人数
     */
    private Integer socialStaffNum;

    /**
     * 企业标签
     */
    private String tags;

    /**
     * 纳税人识别号
     */
    private String taxNumber;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 英文名
     */
    private String property3;

    /**
     * 简称
     */
    private String alias;

    /**
     * 组织机构代码
     */
    private String orgNumber;

    /**
     * 企业状态
     */
    private String regStatus;

    /**
     * 成立日期
     */
    private Date estiblishTime;

    /**
     * 更新时间
     */
    private Date updateTimes;

    /**
     * 股票类型
     */
    private String bondType;

    /**
     * 法人
     */
    private String legalPersonName;

    /**
     * 经营结束时间
     */
    private Date toTime;

    /**
     * 实收注册资金
     */
    private String actualCapital;

    /**
     * 企业类型
     */
    private String companyOrgType;

    /**
     * 组成形式，1-个人经营、2-家庭经营
     */
    private Integer compForm;

    /**
     * 省份简称
     */
    private String base;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 曾用名
     */
    private String historyNames;


    /**
     * 股票号
     */
    private String bondNum;

    /**
     * 注册资本币种 人民币 美元 欧元 等
     */
    private String regCapitalCurrency;

    /**
     * 实收注册资本币种 人民币 美元 欧元 等
     */
    private String actualCapitalCurrency;

    /**
     * 吊销日期
     */
    private Date revokeDate;

    /**
     * 吊销原因
     */
    private String revokeReason;

    /**
     * 注销日期
     */
    private Date cancelDate;

    /**
     * 注销原因
     */
    private String cancelReason;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 网址
     */
    private String websiteList;

    /**
     * 企业联系电话
     */
    private String phoneNumber;

    /**
     *国民经济行业分类门类
     */
    private String category;

    /**
     *国民经济行业分类大类
     */
    private String categoryBig;

    /**
     *国民经济行业分类中类
     */
    private String categoryMiddle;

    /**
     *国民经济行业分类小类
     */
    private String categorySmall;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;
}