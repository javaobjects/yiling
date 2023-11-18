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
public class UpdateB2bOrderIdenRequest extends BaseRequest {

    /**
     * idList（不为空时只按id更新，反之按筛选条件更新）
     */
    @NotNull
    private List<Long> idList;

    /**
     * 卖家eid
     */
    private List<Long> sellerEidList;

    /**
     * 开始签收时间
     */
    private Date startReceiveTime;

    /**
     * 结束签收时间
     */
    private Date endReceiveTime;

    /**
     * 报表计算状态：1-未计算 2-已计算
     */
    private Integer reportSettStatus;

    /**
     * 报表状态
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
