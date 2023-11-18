package com.yiling.marketing.couponactivity.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商家券自主领券规则表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_coupon_activity_enterprise_get_rules")
public class CouponActivityEnterpriseGetRulesDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 活动开始时间
     */
    private Date beginTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 自主领取数量
     */
    private Integer giveNum;

    /**
     * 指定企业类型(1:全部 2:指定)
     */
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    private String conditionEnterpriseTypeValue;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员；5-部分用户）
     */
    private Integer conditionUserType;

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


}
