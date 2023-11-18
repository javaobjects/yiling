package com.yiling.dataflow.flowcollect.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存月流向库存上传数据 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowMonthInventoryRequest extends BaseRequest {

    /**
     * 上传记录ID
     */
    private Long recordId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 库存提取日期
     */
    private Date gbExtractTime;

    /**
     * 商品名称
     */
    private String gbName;

    /**
     * 商品规格
     */
    private String gbSpecifications;

    /**
     * 批号
     */
    private String gbBatchNo;

    /**
     * 数量
     */
    private BigDecimal gbNumber;

    /**
     * 单位
     */
    private String gbUnit;

    /**
     * 单价
     */
    private BigDecimal gbPrice;

    /**
     * 金额
     */
    private BigDecimal gbTotalAmount;

    /**
     * 入库日期
     */
    private Date gbTime;

    /**
     * 生产日期
     */
    private String gbProduceTime;

    /**
     * 有效期
     */
    private String gbEndTime;

    /**
     * 生产厂家
     */
    private String gbManufacturer;

}
