package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateFlowOrderIdenRequest extends BaseRequest {

    /**
     * idList（不为空时只按id更新，反之按筛选条件更新）
     */
    @NotNull
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
     * 修改后的标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    @NotNull
    private Integer updateIdenStatus;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    private Integer abnormalReason;

    /**
     * 异常描述
     */
    private String abnormalDescribed;
}
