package com.yiling.marketing.couponactivityautogive.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 自动发券活动关联优惠券活动DTO
 *
 * @author: houjie.sun
 * @date: 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGiveCouponDTO extends BaseDTO {

    /**
     * 自动发券活动ID
     */
    private Long couponActivityAutoGiveId;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 自动发券数量
     */
    private Integer giveNum;

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
     * 备注
     */
    private String remark;
}
