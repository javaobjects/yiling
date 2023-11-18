package com.yiling.goods.standard.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateStandardCategoryNameRequest extends BaseRequest {
    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类id
     */
    private Long id;


}
