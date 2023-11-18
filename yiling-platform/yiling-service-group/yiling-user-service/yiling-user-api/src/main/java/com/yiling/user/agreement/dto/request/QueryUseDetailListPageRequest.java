package com.yiling.user.agreement.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/8/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUseDetailListPageRequest extends QueryPageListRequest {

	/**
	 * 申请单id
	 */
	@NotNull
	private List<Long> useIdList;
}
