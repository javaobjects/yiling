package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 优惠券活动类型（1-满减券；2-满折券）
     */
    private Integer type;

    /**
     * 优惠券活动类型（1-满减券；2-满折券）
     */
    private String typeStr;

    /**
     * 优惠券活动名称
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
     * 活动分类（1-平台活动；2-商家活动）
     */
    private String sponsorTypeStr;

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
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    private String bearStr;

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
     * 用券开始时间
     */
    private String effectiveTime;

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
    private String platformSelected;

    /**
     * 支付方式限制（1-全部支付方式；2-部分支付方式）
     */
    private Integer payMethodLimit;

    /**
     * 可用支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）
     */
    private String payMethodSelected;

    /**
     * 可叠加促销列表（1-满赠）
     */
    private String coexistPromotion;

    /**
     * 优惠券总数量
     */
    private Integer totalCount;

    /**
     * 优惠券已发放数量
     */
    private Integer giveCount;
    /**
     * 剩余发放数量
     */
    private Integer surplusCount;

    /**
     * 已使用数量
     */
    private Integer useCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private String statusStr;

    /**
     * 活动状态（1-未开始 2-进行中 3-已结束）
     */
    private Integer activityStatus;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    private Integer enterpriseLimit;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    private String enterpriseLimitStr;

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
     * 是否可领取，商家券（1-用户可领 2-用户不可领）
     */
    private Integer canGet;

    /**
     * 修改标识（true-可修改；false-不可修改）
     */
    private Boolean updateFlag;

    /**
     * 停用标识（true-可停用；false-不可停用）
     */
    private Boolean stopFlag;

    /**
     * 作废标识（true-可作废；false-不可作废）
     */
    private Boolean scrapFlag;

    /**
     * 发放标识（true-可发放；false-不可发放）
     */
    private Boolean giveFlag;

    /**
     * 所属企业标识（true-自己企业；false-其他企业，只能查看）
     */
    private Boolean enterpriseFlag;

    /**
     * 复制标识（true-可复制；false-不可复制）
     */
    private Boolean copyFlag;

    /**
     * 增券标识（true-可增券；false-不可增券）
     */
    private Boolean increaseFlag;

    /**
     * 优惠规则
     */
    private String couponRules;

    /**
     * 发放后生效规则，按发放/领取XX天过期
     */
    private String giveOutEffectiveRules;

    /**
     * 可用商品（1-全部商品；2-部分商品）
     */
    private Integer goodsLimit;
    /**
     * 可用商品（1-全部商品；2-部分商品）
     */
    private String goodsLimitStr;

    /**
     * 1 - 全部用户；2 - 仅普通用户；3 - 全部会员用户；4 - 部分指定会员;5 - 部分用户，6新客
     */
    private Integer userType;

    /**
     * 优惠券类型：1-商品优惠券 2-会员优惠券
     */
    private Integer memberType;

    /**
     * 1全部会员方案 ，2部分会员方案
     */
    private Integer memberLimit;

    /**
     * 面值--供积分模块使用
     */
    private String faceValue;
}
