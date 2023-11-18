package com.yiling.goods.medicine.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerLimitRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    private Long eid;

    private List<Long> customerEids;

    private Integer limitFlag;

    private Integer recommendationFlag;
}
