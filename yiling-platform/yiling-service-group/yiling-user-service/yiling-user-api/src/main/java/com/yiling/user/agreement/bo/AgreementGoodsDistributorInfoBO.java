package com.yiling.user.agreement.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 协议商品库存信息BO
 * @author dexi.yao
 * @date 2021-06-15
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AgreementGoodsDistributorInfoBO implements Serializable {
    private static final long serialVersionUID = -9044217574008714L;
	/**
	 * 协议主体ID（甲方）
	 */
	private Long eid;

	/**
	 * 主体名称
	 */
	private String ename;

	/**
	 * 协议客户ID（乙方）
	 */
	private Long secondEid;

	/**
	 * 乙方企业名称
	 */
	private String secondName;

	/**
	 * 乙方渠道类型名称
	 */
	private String secondChannelName;

	/**
	 * 第三方客户ID（丙方）
	 */
	private Long thirdEid;

	/**
	 * 丙方名称
	 */
	private String thirdName;

	/**
	 * 协议类型 1-双方协议 2-三方协议
	 */
	private Integer type;

	/**
	 * 商品id
	 */
	private Long goodsId;

}
