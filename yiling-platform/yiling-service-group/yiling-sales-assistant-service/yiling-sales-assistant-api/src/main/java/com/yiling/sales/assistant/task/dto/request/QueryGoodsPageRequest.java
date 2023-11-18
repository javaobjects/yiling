package com.yiling.sales.assistant.task.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPageRequest extends QueryPageListRequest {
    private Long goodsId;

    private String goodsName;

    private String manufacturer;

    private String supplier;

    private Integer taskType;

    private List<Long> distributorEidList;
}