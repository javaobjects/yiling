package com.yiling.hmc.wechat.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 拿药计划DTO
 *
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceFetchPlanDTO extends BaseDTO {

    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 保单支付记录表id
     */
    private Long recordPayId;

    /**
     * 初始拿药时间
     */
    private Date initFetchTime;

    /**
     * 实际拿药时间
     */
    private Date actualFetchTime;

    /**
     * 拿药状态 1-已拿，2-未拿
     */
    private Integer fetchStatus;

    /**
     * 关联兑付订单id
     */
    private Long orderId;

    /**
     * 是否删除 0-否，1-是
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
