package com.yiling.marketing.payPromotion.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付促销主表
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_pay_promotion_activity")
public class MarketingPayPromotionActivityDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动；）
     */
    private Integer sponsorType;

    /**
     * 生效开始时间
     */
    private Date beginTime;

    /**
     * 生效结束时间
     */
    private Date endTime;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 运营备注
     */
    private String operatingRemark;

    /**
     * 费用承担方（1-平台承担；2-商家承担；）
     */
    private Integer bear;

    /**
     * 商家范围类型（1-全部商家；2-指定商家；）
     */
    private Integer conditionSellerType;

    /**
     * 商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）
     */
    private Integer conditionGoodsType;

    /**
     * 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
     */
    private Integer conditionBuyerType;

    /**
     * 指定企业类型(1:全部类型 2:指定类型 3:指定范围类型)
     */
    private Integer conditionEnterpriseType;

    /**
     * 指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）
     */
    private String conditionEnterpriseTypeValue;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分会员）
     */
    private Integer conditionUserType;

    /**
     * 指定部分会员用户类型（1-指定方案会员；2-指定推广方会员）逗号隔开
     */
    private String conditionUserMemberType;

    /**
     * 其他(1-新客适用,多个值用逗号隔开)
     */
    private String conditionOther;

    /**
     * 支付方式(1-在线支付;2-线下支付；3-账期逗号隔开)
     */
    private String payType;

    /**
     * 生效条件(1-按金额)
     */
    private Integer conditionEffective;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 单个支付促销一共使用多少次
     */
    private BigDecimal totalNum;

    /**
     * 每个客户参数次数
     */
    private Integer maxGiveNum;

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
     * ''创建人手机号''
     */
    private String createTel;

    /**
     * ''创建人名称''
     */
    private String createUserName;

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
}
