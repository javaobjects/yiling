package com.yiling.framework.common.web.rest;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 数组对象
 *
 * @author xuan.zhou
 * @date 2020/12/14
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel("数组对象")
public class ArrayObject<T> {

    T[] list;
}
