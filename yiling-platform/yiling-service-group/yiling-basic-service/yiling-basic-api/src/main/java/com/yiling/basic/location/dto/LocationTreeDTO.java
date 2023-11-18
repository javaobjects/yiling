package com.yiling.basic.location.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 区域树 DTO
 */
@Data
@Accessors(chain = true)
public class LocationTreeDTO implements Serializable {

    private static final long serialVersionUID = -944940362317407121L;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 上级区域编码
     */
    private String parentCode;

    /**
     * 排序优先级
     */
    private Integer priority;

    /**
     * 下级区域列表
     */
    private List<LocationTreeDTO> children;
}
