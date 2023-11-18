package com.yiling.marketing.coupon.entity;

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
 * 用户优惠券信息表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_coupon")
public class CouponDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    private String couponActivityName;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 获取方式（1-运营发放；2-自动发放；3-自主领取；4-促销活动赠送）
     */
    private Integer getType;

    /**
     * 平台自动发券/自主领券活动ID
     */
    private Long couponActivityAutoId;

    /**
     * 商家自主领券活动ID
     */
    private Long couponActivityBusinessAutoId;

    /**
     * 自动发券/自主领券活动名称
     */
    private String couponActivityAutoName;

    /**
     * 发放/领取人ID
     */
    private Long getUserId;

    /**
     * 发放/领取人人姓名
     */
    private String getUserName;

    /**
     * 发放/领取时间
     */
    private Date getTime;

    /**
     * 用券人ID
     */
    private Long userId;

    /**
     * 用券人姓名
     */
    private String userName;

    /**
     * 用券时间
     */
    private Date useTime;

    /**
     * 优惠券生效时间
     */
    private Date beginTime;

    /**
     * 优惠券失效时间
     */
    private Date endTime;

    /**
     * 使用状态：1-未使用；2-已使用
     */
    private Integer usedStatus;

    /**
     * 状态（1-正常；2-废弃）
     */
    private Integer status;

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
