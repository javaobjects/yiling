package com.yiling.sales.assistant.commissions.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售助手用户佣金DTO
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommissionsUserDTO extends BaseDTO {


	private static final long serialVersionUID = -7180864984045780849L;

	/**
     * 用户id
     */
    private Long userId;

	/**
	 * 累计佣金金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 以结算金额
	 */
	private BigDecimal paidAmount;

	/**
	 * 待结算金额
	 */
	private BigDecimal surplusAmount;

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

    /**
     * 备注
     */
    private String remark;


}
