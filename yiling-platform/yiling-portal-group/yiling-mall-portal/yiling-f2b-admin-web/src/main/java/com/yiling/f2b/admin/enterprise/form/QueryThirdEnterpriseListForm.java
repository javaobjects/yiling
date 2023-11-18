package com.yiling.f2b.admin.enterprise.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业采购关系 Form
 *
 * @author: dexi.yao
 * @date: 2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryThirdEnterpriseListForm extends QueryPageListForm {

    /**
     * 采购商ID
     */
    @ApiModelProperty("采购商ID")
	@NotNull
    private Long buyerEid;

	/**
	 * 渠道商名称
	 */
	@ApiModelProperty("渠道商名称")
	private String sellerName;

	/**
	 * 渠道商ID
	 */
	@ApiModelProperty("渠道商ID")
	@Min(value = 0L,message = "id必须大于0")
	private Long sellerEid;

}
