package com.yiling.user.agreement.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageListByIdRequest extends QueryPageListRequest {

	/**
	 * 主键id集合
	 */
	@NotNull
	private List<Long> idList;
}
