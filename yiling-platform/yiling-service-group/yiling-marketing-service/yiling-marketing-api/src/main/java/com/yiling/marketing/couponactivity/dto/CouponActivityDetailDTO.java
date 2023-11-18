package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * B2B-优惠券详情DTO
 *
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityDetailDTO extends BaseDTO {

    /**
     * 复制的优惠券活动id
     */
    private Long oldId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 类型（1-满减券；2-满折券）
     */
    private Integer type;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券描述
     */
    private String description;

    /**
     * 预算编号
     */
    private String budgetCode;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 使用门槛（1-支付金额满额）
     */
    private Integer threshold;

    /**
     * 门槛金额/件数
     */
    private BigDecimal thresholdValue;

    /**
     * 优惠内容（金额）
     */
    private BigDecimal discountValue;

    /**
     * 最高优惠额度（折扣券）
     */
    private BigDecimal discountMax;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    private Integer bear;

    /**
     * 会员方案限制，1全部会员方案，2部分会员方案
     */
    private Integer memberLimit;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 用券时间（1-固定时间；2-发放领取后生效）
     */
    private Integer useDateType;

    /**
     * 用券开始时间
     */
    private Date beginTime;

    /**
     * 用券结束时间
     */
    private Date endTime;

    /**
     * 发放/领取xx天后失效
     */
    private Integer expiryDays;

    /**
     * 平台限制（1-全部平台；2-部分平台）
     */
    private Integer platformLimit;

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    private List<String> platformSelectedList;

    /**
     * 支付方式限制（1-全部支付方式；2-部分支付方式）
     */
    private Integer payMethodLimit;

    /**
     * 可用支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）
     */
    private List<String> payMethodSelectedList;

    /**
     * 可叠加促销列表（1-满赠）
     */
    private List<String> coexistPromotionList;

    /**
     * 优惠券总数量
     */
    private Integer totalCount;

    /**
     * 已使用数量
     */
    private Integer useCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 活动是否已开始：true-已开始 false-未开始
     */
    private Boolean running;

    /**
     * 修改名称、预算编号标识（true-可修改；false-不可修改）
     */
    private Boolean editNameFlag;

    /**
     * 修改商家设置、商品设置标识（true-可修改；false-不可修改）
     */
    private Boolean editLimitFlag;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    private Integer enterpriseLimit;

    /**
     * 可用商品（1-全部商品可用；2-部分商品可用）
     */
    private Integer goodsLimit;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

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
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 修改人姓名
     */
    private String updateUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发放后生效规则，按发放/领取XX天过期
     */
    private String giveOutEffectiveRules;

    /**
     * 是否可领取，商家券（1-用户可领 2-用户不可领）
     */
    private Integer canGet;

// ****************************** 商家后台 用户领取设置 ******************************

    /**
     * 活动开始时间
     */
    private Date activityBeginTime;

    /**
     * 活动结束时间
     */
    private Date activityEndTime;

    /**
     * 可领取数量
     */
    private Integer giveNum;

    /**
     * 指定企业类型(1-全部；2-指定)
     */
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    private List<String> conditionEnterpriseTypeValueList;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员；5-部分用户）
     */
    private Integer conditionUserType;

    /**
     * 支付方式
     */
    private String payMethodSelected;

    /**
     * 优惠券对应的eid
     */
    private Long realEid;

    /**
     * 优惠券类型：1-商品优惠券 2-会员优惠券
     */
    private Integer memberType;

    /**
     * 剩余数量 总的减去使用的
     */
    private Integer surplusCount;
}
