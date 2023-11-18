package com.yiling.goods.standard.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCategoryNameRequest extends BaseRequest {

    private static final long serialVersionUID = -33312528332221L;

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;


}
