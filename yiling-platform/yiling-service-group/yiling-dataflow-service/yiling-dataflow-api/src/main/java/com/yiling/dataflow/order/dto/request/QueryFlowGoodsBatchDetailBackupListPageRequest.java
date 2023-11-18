package com.yiling.dataflow.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/12/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsBatchDetailBackupListPageRequest extends QueryPageListRequest {

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

    /**
     * 备份表名称
     */
    private String tableName;

}
