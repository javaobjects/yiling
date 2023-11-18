package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返利申请表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateApplyDTO extends BaseDTO {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业账号
     */
    private String easCode;

    /**
     * 申请单号
     */
    private String code;

    /**
     * 申请总金额
     */
    private BigDecimal totalAmount;

    /**
     * 所属年度
     */
    private Integer year;

    /**
     * 所属月度（1-12）、季度（1-4）、其余该值为 0
     */
    private Integer month;

    /**
     * 月度类型 1-月度 2-季度 3-上半年 4-下半年 5-全年
     */
    private Integer rangeType;

	/**
	 * 入账企业id
	 */
	private Long entryEid;

	/**
	 * 入账企业名称
	 */
	private String entryName;

	/**
	 * 入账企业easCode
	 */
	private String entryCode;

    /**
     * 省份code
     */
    private String provinceCode;

    /**
     * 省份
     */
    private String provinceName;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 状态 1-待审核 2-已入账 3-驳回
     */
    private Integer status;

    /**
     * 审核原因
     */
    private String auditRemark;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建人工号
	 */
	private String createUserCode;

    /**
     * 备注
     */
    private String remark;


}
