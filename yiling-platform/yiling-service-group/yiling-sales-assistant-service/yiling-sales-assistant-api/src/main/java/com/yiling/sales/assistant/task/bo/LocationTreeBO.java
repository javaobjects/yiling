package com.yiling.sales.assistant.task.bo;

import java.util.List;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/3/2
 */
@Data
public class LocationTreeBO {
    private String code;

    private List<LocationTreeBO> children;
}