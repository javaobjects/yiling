package com.yiling.goods.ylprice.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@Data
@Accessors(chain = true)
public class QueryReportPricePageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -3342031723708365576L;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    private Long paramId;

    private List<Long> userIds;
}
