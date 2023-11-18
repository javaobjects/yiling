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
 * 优惠券活动操作日志表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_coupon_activity_log")
public class CouponActivityLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 类型（1-新增；2-复制；3-修改；4-手动发放；5-自动发放；6-自主领取）
     */
    private Integer type;

    /**
     * 状态（1-成功；2-失败）
     */
    private Integer status;

    /**
     * 发放/领取数量
     */
    private Integer giveNum;

    /**
     * 失败原因
     */
    private String faileReason;

    /**
     * 内容json串
     */
    private String jsonContent;

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
