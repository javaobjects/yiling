package com.yiling.framework.common.web.rest;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ID对象
 *
 * @author xuan.zhou
 * @date 2020/12/14
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel("ID对象")
public class IdObject<Serializable> {

    Serializable id;
}
