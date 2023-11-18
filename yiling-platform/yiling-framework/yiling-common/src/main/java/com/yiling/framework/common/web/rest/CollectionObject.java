package com.yiling.framework.common.web.rest;

import java.util.Collection;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 集合对象
 *
 * @author xuan.zhou
 * @date 2020/12/14
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel("集合对象")
public class CollectionObject<T> {

    Collection<T> list;
}
