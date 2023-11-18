package com.yiling.f2b.admin.agreement.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebateSystemUrlVO  extends BaseVO {

	@ApiModelProperty(value = "跳转路径")
	private String url;
}
