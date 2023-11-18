package com.yiling.settlement.report.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExportB2bOrderSyncRequest extends BaseRequest {

    /**
     * id集合
     */
    private String idList;

    /**
     * 商业eid
     */
    private Long eid;

    /**
     * 省code
     */
    private String provinceCode;

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 区code
     */
    private String regionCode;

    /**
     * 开始签收时间
     */
    @NotNull
    private Date startReceiveTime;

    /**
     * 结束签收时间
     */
    @NotNull
    private Date endReceiveTime;

    /**
     * 报表状态 0-待返利 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private Integer reportStatus;

    /**
     * 标识状态：1-正常订单 2-无效订单 3-异常订单
     */
    private Integer identificationStatus;
}