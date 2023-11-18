package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowOrderPageListRequest extends QueryPageListRequest {

    /**
     * id集合
     */
    private List<Long> idList;

    /**
     * 商业代码（商家eid） PS不建议多个会导致索引效率降低
     */
    private List<Long> eidList;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private List<String> soSourceList;

    /**
     * 销售日期
     */
    private Date startSoTime;

    /**
     * 销售日期
     */
    private Date endSoTime;

    /**
     * 报表状态：0-待返利 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private List<Integer> reportStatusList;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 是否过滤掉无效订单(1:过滤)
     */
    private Integer filterInvalidOrder;

}
