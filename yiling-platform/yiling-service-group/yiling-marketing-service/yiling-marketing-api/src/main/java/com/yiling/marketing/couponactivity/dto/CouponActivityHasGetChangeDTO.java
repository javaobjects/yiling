package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityHasGetChangeDTO extends BaseDTO {

    /**
     * 优惠券真正关联的eid
     */
    private Long realEid;

    /**
     * 企业ID不同表的别名
     */
    private Long caEid;

    /**
     * 企业ID不同表的别名
     */
    private Long caglEid;

    /**
     * 产品限制
     */
    private Integer caGoodsLimit;

    /**
     * 平台限制
     */
    private Integer caPlatform;

    /**
     * 平台限制
     */
    private Integer userType;

    /**
     * 企业ID1
     */
    private Long caelEid;

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
     * 最高优惠额度（折扣券，保存不能输入0、可以输入空，表数据默认值0表示不限制最高优惠额度）
     */
    private BigDecimal discountMax;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
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
     * 已发放/领取数量
     */
    private Integer giveCount;

    /**
     * 已使用数量
     */
    private Integer useCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 可用供应商（1-全部供应商；2-部分供应商）
     */
    private Integer enterpriseLimit;

    /**
     * 可用商品（1-全部商品；2-部分商品）
     */
    private Integer goodsLimit;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否可领取，商家券（1-用户可领 2-用户不可领）
     */
    private Integer canGet;

    /**
     * 1全部会员方案可用，2部分可用
     */
    private Integer memberLimit;
}
