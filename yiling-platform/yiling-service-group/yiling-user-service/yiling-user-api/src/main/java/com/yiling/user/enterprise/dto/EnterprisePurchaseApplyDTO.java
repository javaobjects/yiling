package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业采购申请 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterprisePurchaseApplyDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 采购商企业ID
     */
    private Long customerEid;

    /**
     * 供应商企业ID
     */
    private Long eid;

    /**
     * 审核状态：1-待审核 2-已建采 3-已驳回
     */
    private Integer authStatus;

    /**
     * 审核人
     */
    private Long authUser;

    /**
     * 审核时间
     */
    private Date authTime;

    /**
     * 驳回原因
     */
    private String authRejectReason;

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
