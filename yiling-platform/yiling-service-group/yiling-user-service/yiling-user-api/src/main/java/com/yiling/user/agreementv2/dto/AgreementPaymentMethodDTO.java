package com.yiling.user.agreementv2.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议付款方式 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementPaymentMethodDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑
     */
    private Integer payMethod;

    /**
     * 占比
     */
    private Integer ratio;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

}
