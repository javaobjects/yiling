package com.yiling.dataflow.sale.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRelationShipByPoCodeRequest extends QueryPageListRequest {

    /**
     * postCode
     */
    private List<Long> postCodeList;
}
