package com.yiling.framework.common.base;

import lombok.Data;

/**
 * Base DTO
 *
 * @author: xuan.zhou
 * @date: 2020/6/16
 */
@Data
public class BaseDTO implements java.io.Serializable {

    private static final long serialVersionUID = -7631547950698054406L;

    /**
     * ID
     */
    private Long id;
}
