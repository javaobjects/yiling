package com.yiling.marketing.couponactivityautoget.entity;

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
 * 自主领券活动会员企业限制表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_coupon_activity_auto_get_enterprise_limit")
public class CouponActivityAutoGetEnterpriseLimitDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 自主领券活动ID
     */
    private Long couponActivityAutoGetId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

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
