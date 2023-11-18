package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目引用业务线
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCategoryDisplayLineRequest extends BaseRequest {


    private static final long serialVersionUID = 8900048486840028889L;
    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 业务线名称
     */
    private String lineName;


}
