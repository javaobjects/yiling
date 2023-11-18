package com.yiling.ih.common;

import java.util.List;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/6/8
 */
@Data
public class ApiPage <T>{
    private Integer total;

    private List<T> list;
}