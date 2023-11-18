package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.Date;

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
public class AgreementApplyDetailOpenDTO extends BaseDTO {

    /**
     * 申请单id
     */
    private Long applyId;

    /**
     * 申请单号
     */
    private String applyCode;

	/**
	 * 明细类型 1-协议类型 2-其他
	 */
	private Integer detailType;

	/**
	 * 协议id
	 */
	private Long agreementId;

	/**
	 * 冲红金额
	 */
	private BigDecimal amount;

	/**
	 * 销售组织code
	 */
	private String sellerCode;

	/**
	 * 销售组织
	 */
	private String sellerName;

    /**
     * 协议名称
     */
    private String agreementName;

    /**
     * 协议描述
     */
    private String agreementContent;

    /**
     * 返利种类
     */
    private String rebateCategory;

    /**
     * 费用科目
     */
    private String costSubject;

    /**
     * 费用归属部门
     */
    private String costDept;

    /**
     * 执行部门
     */
    private String executeDept;

    /**
     * 批复代码
     */
    private String replyCode;

    /**
     * 申请时间
     */
    private Date createTime;

}
