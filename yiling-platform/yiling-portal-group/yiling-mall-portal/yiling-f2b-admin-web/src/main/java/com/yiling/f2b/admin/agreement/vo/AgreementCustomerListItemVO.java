package com.yiling.f2b.admin.agreement.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 协议首页VO
 * @author: shuang.zhang
 * @date: 2021/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("协议首页VO")
public class AgreementCustomerListItemVO extends BaseVO {

    /**
     * 客户ID
     */
    @ApiModelProperty("客户ID")
    private Long customerEid;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 商务联系人个数
     */
    @ApiModelProperty("商务联系人个数")
    private Long customerContactNum;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

	/**
	 * 渠道ID
	 */
	@ApiModelProperty(value = "渠道ID")
	private Long channelId;

    @ApiModelProperty(value = "年度协议个数")
    private Integer yearAgreementCount;

    @ApiModelProperty(value = "临时协议个数(双方)")
    private Integer tempAgreementCount;

    @ApiModelProperty(value = "三方协议总数")
    private Integer thirdAgreementCount;

    @ApiModelProperty(value = "与第三方签订的渠道商个数")
    private Integer thirdAgreementsOtherCount;

}
