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
public class SaveDictDataRequest extends BaseRequest {

    /**
     * 字典类型ID（关联dict_type.id字段）
     */
    private Long typeId;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典键值
     */
    private String value;

    /**
     * 字典描述
     */
    private String description;

    /**
     * 字典排序
     */
    private Integer sort;
}