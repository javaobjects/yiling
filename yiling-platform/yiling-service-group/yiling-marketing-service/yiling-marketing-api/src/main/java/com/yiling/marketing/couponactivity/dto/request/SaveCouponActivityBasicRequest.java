package com.yiling.marketing.couponactivity.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveCouponActivityBasicRequest extends BaseRequest {

    /**
     * 优惠券活动id
     */
    private Long id;

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
    @NotNull
    private Integer type;

    /**
     * 优惠券名称
     */
    @NotEmpty
    private String name;

    /**
     * 优惠券描述
     */
    private String description;

    /**
     * 预算编号
     */
    @NotEmpty
    private String budgetCode;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @NotNull
    private Integer sponsorType;

    /**
     * 使用门槛（1-支付金额满额）
     */
    @NotNull
    private Integer threshold;

    /**
     * 门槛金额/件数
     */
    @NotNull
    private BigDecimal thresholdValue;

    /**
     * 最高优惠额度（折扣券）
     */
    private BigDecimal discountMax;

    /**
     * 优惠内容（金额）
     */
    @NotNull
    private BigDecimal discountValue;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    @NotNull
    private Integer bear;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 有效期（1-固定时间；2-发放领取后生效）
     */
    @NotNull
    private Integer useDateType;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

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
     * 备注
     */
    private String remark;

    /**
     * 会员规格id数组
     */
    private List<Long> ids;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 生成优惠券总数量
     */
    private Integer totalCount;

    /**
     * 1商品 2会员优惠券
     */
    private Integer memberType;

    /**
     * 1全部会员方案可用，2部分可用
     */
    private Integer memberLimit;
}
