package com.yiling.goods.standard.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author shichen
 * @类名 CreateStandardTagRequest
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
public class CreateStandardTagRequest extends BaseRequest {
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 类型：1-手动标签
     */
    private Integer type;
}
