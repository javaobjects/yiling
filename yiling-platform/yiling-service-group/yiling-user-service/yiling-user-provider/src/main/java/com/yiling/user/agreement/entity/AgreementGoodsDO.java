package com.yiling.user.agreement.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议商品表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_goods")
public class AgreementGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

	/**
	 * 协议主键
	 */
	private Long agreementId;

	/**
	 * 商品id
	 */
	private Long goodsId;

	/**
	 * 标准库ID
	 */
	private Long standardId;

	/**
	 * 售卖规格ID
	 */
	private Long sellSpecificationsId;

	/**
	 * 商品名称
	 */
	private String goodsName;

    /**
     * 商品标准库
     */
    private String standardGoodsName;

    /**
     * 标准库批准文号
     */
    private String standardLicenseNo;

	/**
	 * 售卖规格
	 */
	private String sellSpecifications;

	/**
	 * 批准文号
	 */
	private String licenseNo;

	/**
	 * 专利类型 1-非专利 2-专利
	 */
	private Integer isPatent;

	/**
	 * 是否删除：0-否 1-是
	 */
	@TableLogic
	private Integer delFlag;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long createUser;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updateUser;

	/**
	 * 备注
	 */
	private String remark;


}
