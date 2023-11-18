package com.yiling.basic.dict.bo.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveDictTypeRequest extends BaseRequest {
   
    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典描述
     */
    private String description;
}
