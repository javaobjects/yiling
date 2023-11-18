package com.yiling.settlement.report.dto.request;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExportReportPurchaseStockRequest extends BaseRequest {

    /**
     * 商业名称
     */
    private Long eid;

    /**
     * 以岭品id
     */
    private Long ylGoodsId;

}
