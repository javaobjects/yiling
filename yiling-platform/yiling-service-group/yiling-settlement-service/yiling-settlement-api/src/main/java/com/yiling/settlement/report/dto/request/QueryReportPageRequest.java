package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportPageRequest extends QueryPageListRequest {

    /**
     * 商业代码（商家eid） PS不建议多个会导致索引效率降低
     */
    private List<Long> eidList;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    private Integer type;

    /**
     * 报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private List<Integer> status;

    /**
     * 开始创建时间
     */
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    private Date endCreateTime;

    /**
     * 报表id集合
     */
    private List<Long> reportIdList;

    /**
     * 订单返利状态：1-待返利 2-已返利 3-部分返利
     */
    private Integer rebateStatus;

}
