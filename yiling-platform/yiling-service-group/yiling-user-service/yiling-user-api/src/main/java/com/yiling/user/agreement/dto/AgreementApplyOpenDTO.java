package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementApplyOpenDTO extends BaseDTO {

    /**
     * 申请单id
     */
    private Long applyId;

    /**
     * 申请单号
     */
    private String applyCode;

    /**
     * 入账企业code
     */
    private String entryCode;

    /**
     * 入账企业
     */
    private String entryName;

    /**
     * 所属企业code
     */
    private String ownerCode;

    /**
     * 所属企业
     */
    private String ownerName;

    /**
     * 冲红金额
     */
    private BigDecimal totalAmount;

	/**
	 * 省份
	 */
	private String provinceName;

	/**
	 * 年度
	 */
	private String year;

	/**
	 * 月份
	 */
	private String month;

	/**
	 * 商品
	 */
	private String goodsName;

    /**
     * 申请时间
     */
    private Date createTime;

    /**
     * 申请人
     */
    private Long createUser;

    /**
     * 申请人工号
     */
    private String createUserCode;

    /**
     * 入账时间
     */
    private Date entryTime;

	/**
	 * 申请明细
	 */
	private List<AgreementApplyDetailOpenDTO> details;


}
