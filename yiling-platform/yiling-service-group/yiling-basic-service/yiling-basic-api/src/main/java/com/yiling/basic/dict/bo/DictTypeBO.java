package com.yiling.basic.dict.bo;

import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
@Data
public class DictTypeBO implements java.io.Serializable {
    /**
     *字典类型表id
     */
    private Long id;
    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典描述
     */
    private String description;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

}
