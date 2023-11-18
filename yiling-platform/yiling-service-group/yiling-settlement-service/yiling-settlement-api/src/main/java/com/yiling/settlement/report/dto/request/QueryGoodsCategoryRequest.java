package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/25
 */
@Data
@Accessors(chain = true)
public class QueryGoodsCategoryRequest extends BaseRequest {
    private static final long serialVersionUID = -8080666528501920666L;

    /**
     * 商品Id
     */
    private List<Long> goodsIds;

    /**
     * 时间
     */
    private Date date;
}