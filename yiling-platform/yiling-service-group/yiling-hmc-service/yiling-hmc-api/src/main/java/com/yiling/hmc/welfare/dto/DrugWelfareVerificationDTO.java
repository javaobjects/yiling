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
public class DrugWelfareVerificationDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 药品福利id
     */
    private Long drugWelfareId;

    /**
     * 入组福利券表id
     */
    private Long drugWelfareGroupCouponId;

    /**
     * b2b优惠券id
     */
    private Long couponId;


    /**
     * 核销时间
     */
    private Date verificationTime;

    /**
     * 备注
     */
    private String remark;

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

}
