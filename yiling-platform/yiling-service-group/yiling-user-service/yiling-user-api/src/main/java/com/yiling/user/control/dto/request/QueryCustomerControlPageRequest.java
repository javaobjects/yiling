package com.yiling.user.control.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/11/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerControlPageRequest  extends QueryPageListRequest {

    private Long eid;

    private String name;

    private String provinceCode;

    private String cityCode;

    private String regionCode;

    private Integer type;

    private Long customerGroupId;

    private Long goodsId;
}
