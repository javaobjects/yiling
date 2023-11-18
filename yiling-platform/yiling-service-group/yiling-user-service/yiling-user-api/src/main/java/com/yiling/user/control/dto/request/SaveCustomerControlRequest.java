package com.yiling.user.control.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCustomerControlRequest extends BaseRequest {

    private Long id;

    private Integer customerSet;

    private List<Long> customerIds;

    private Long goodsId;

    private Long eid;

}

