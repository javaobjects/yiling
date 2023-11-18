package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 协议兑付日志DTO
 *
 * @author dexi.yao
 * @date 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateLogDTO extends BaseDTO {


	private static final long serialVersionUID = 2602658821769588611L;

	/**
     * 兑付者的easCode
     */
    private String easCode;

    /**
     * 日志记录名称
     */
    private String logName;

    /**
     * 协议id
     */
    private Long agreementId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 兑付类型：1-协议兑付 2-其他
     */
    private Integer cashType;

    /**
     * 兑付以前的金额
     */
    private BigDecimal beforeTotalAmount;

    /**
     * 兑付金额
     */
    private BigDecimal discountAmount;

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


}
