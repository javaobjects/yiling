package com.yiling.hmc.welfare.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/10/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareGroupCouponStatisticsPageDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 福利计划id
     */
    private Long drugWelfareId;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家销售人员id
     */
    private Long sellerUserId;

    /**
     * 服药人姓名
     */
    private String medicineUserName;

    /**
     * 服药人手机号
     */
    private String medicineUserPhone;

    /**
     * 入组id
     */
    private Long joinGroupId;

    /**
     * 入组时间
     */
    private Date createTime;

    /**
     * 药品福利入组id
     */
    private Long groupId;

    /**
     * 福利券id
     */
    private Long drugWelfareCouponId;

    /**
     * 福利券码
     */
    private String couponCode;

    /**
     * 福利券状态 1-待激活，2-已激活，3-已使用
     */
    private Integer couponStatus;

    /**
     * 要求达到的数量,满几盒
     */
    private Long requirementNumber;

    /**
     * 赠送数量,赠几盒
     */
    private Long giveNumber;

    /**
     * b2b优惠券id
     */
    private Long couponId;

    /**
     * 激活时间
     */
    private Date activeTime;

    /**
     * 核销时间
     */
    private Date verifyTime;
}
