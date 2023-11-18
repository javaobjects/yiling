package com.yiling.marketing.couponactivityautogive.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自动发券企业参与记录表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_coupon_activity_auto_give_record")
@ToString
public class CouponActivityAutoGiveRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 自动发券活动ID
     */
    private Long couponActivityAutoGiveId;

    /**
     * 自动发券活动名称
     */
    private String couponActivityAutoGiveName;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 惠券活动名称
     */
    private String couponActivityName;

    /**
     * 获取方式（1-运营发放；2-自动发放）
     */
    private Integer getType;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券；3-推广企业自动发券）
     */
    private Integer type;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 发放数量
     */
    private Integer giveNum;

    /**
     * 发放状态（1-已发放；2-发放失败）
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String faileReason;

    /**
     * 累计金额
     */
    private BigDecimal cumulativeAmount;

    /**
     * 所属企业ID
     */
    private Long ownEid;

    /**
     * 所属企业名称
     */
    private String ownEname;

    /**
     * 发放批次号
     */
    private String batchNumber;

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
