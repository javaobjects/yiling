package com.yiling.export.export.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
@Data
public class ExportSearchConditionDTO implements Serializable {

    private static final long serialVersionUID = 6151776504405188746L;

    /**
     * 字段的英文名称
     */
    private String name;

    /**
     * 字段的中文名
     */
    private String desc;

    /**
     * 字段的值
     */
    private String value;

    /**
     * 字段的值中文描述。比如：0否1是
     */
    private String valueDescription;

    /**
     * 是否显示。隐藏字段传0，显示字段传1
     */
    private Integer visibility;

}
