package com.yiling.basic.location.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 行政区划 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LocationDTO extends BaseDTO {

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
     * 状态：1-启用 2-停用
     */
    private Integer status;
}
