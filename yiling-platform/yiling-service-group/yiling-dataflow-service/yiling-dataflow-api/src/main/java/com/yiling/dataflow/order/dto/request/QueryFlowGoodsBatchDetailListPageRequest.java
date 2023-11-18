package com.yiling.dataflow.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsBatchDetailListPageRequest extends QueryPageListRequest {

    /**
     * 负责的公司
     */
    private List<Long> eidList;

    private Date startTime;

    private Date endTime;

    /**
     * 有无标准库规格关系：0-无, 1-有
     */
    private Integer specificationIdFlag;

    private Integer goodsCrmCodeFlag;

    /**
     * 当前库存月份
     */
    private String gbDetailMonth;

    private Long eid;

    private Integer timeType;

    private String goodsName;

    private String specifications;

    private Integer selectYear;

}
