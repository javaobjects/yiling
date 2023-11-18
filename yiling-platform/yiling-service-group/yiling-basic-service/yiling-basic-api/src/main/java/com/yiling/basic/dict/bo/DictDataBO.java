package com.yiling.basic.dict.bo;

import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
@Data
public class DictDataBO implements java.io.Serializable {

    /**
     * 字典内容Id
     */
    private Long id;
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
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 字典排序
     */
    private Integer sort;

}
