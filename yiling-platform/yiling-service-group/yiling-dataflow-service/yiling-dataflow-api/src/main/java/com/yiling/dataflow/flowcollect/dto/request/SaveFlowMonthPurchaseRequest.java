package com.yiling.dataflow.flowcollect.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存月流向采购上传数据 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowMonthPurchaseRequest extends BaseRequest {

    /**
     * 上传记录ID
     */
    private Long recordId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 购进日期
     */
    private Date poTime;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 产品规格
     */
    private String poSpecifications;

    /**
     * 批号
     */
    private String poBatchNo;

    /**
     * 供应商名称
     */
    private String enterpriseName;

    /**
     * 库存
     */
    private BigDecimal poQuantity;

    /**
     * 单位
     */
    private String poUnit;

    /**
     * 单价
     */
    private BigDecimal poPrice;

    /**
     * 金额
     */
    private BigDecimal poTotalAmount;

    /**
     * 生产厂家
     */
    private String poManufacturer;

    /**
     * 采购员
     */
    private String poBuyer;

}
