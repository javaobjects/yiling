package com.yiling.hmc.welfare.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 入组券包DTO
 * @author benben.jia
 * @date 2022-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareGroupCouponVerificationDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;


     /**
     * 状态：1券码输入错误 2已经核销 3活动不存在 4活动未开始 5活动已过期 6活动已结束 7核销成功
     */
    private Integer status;

     /**
     * 用药福利计划名称
     */
    private String drugWelfareName;

     /**
     * 入组姓名
     */
    private String medicineUserName;

     /**
     * 入组id
     */
    private Long joinGroupId;


    /**
     * 福利计划id
     */
    private Long drugWelfareId;



    /**
     * 福利计划id
     */
    private Long couponId;

}
