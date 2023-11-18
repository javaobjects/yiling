package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.yiling.framework.common.base.request.BaseRequest;

/**
 * @author: dexi.yao
 * @date: 2022/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExportFlowOrderSyncRequest extends BaseRequest {

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
     * 开始下单时间
     */
    private Date startSoTime;

    /**
     * 结束下单时间
     */
    private Date endSoTime;

    /**
     * 报表状态 0-待返利 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private Integer reportStatus;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private List<String> soSourceList;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;
}