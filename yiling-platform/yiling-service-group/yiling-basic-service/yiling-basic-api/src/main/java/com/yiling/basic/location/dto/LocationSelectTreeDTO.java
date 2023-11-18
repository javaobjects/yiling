package com.yiling.basic.location.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 区域选择树 DTO
 *
 * @author: xuan.zhou
 * @date: 2022/3/9
 */
@Data
@Accessors(chain = true)
public class LocationSelectTreeDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1537197021481298307L;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 是否选中
     */
    private Boolean selectedFlag;

    /**
     * 下级区域列表
     */
    private List<LocationSelectTreeDTO> children;
}
