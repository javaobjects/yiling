package com.yiling.dataflow.flowcollect.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存月流向销售上传数据 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowMonthSalesRequest extends BaseRequest {

    /**
     * 上传记录ID
     */
    private Long recordId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 产品名称
     */
    private String soSpecifications;

    /**
     * 批号
     */
    private String soBatchNo;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 数量
     */
    private BigDecimal soQuantity;

    /**
     * 单位
     */
    private String soUnit;

    /**
     * 单价
     */
    private BigDecimal soPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 生产厂家
     */
    private String soManufacturer;

    /**
     * 开票员
     */
    private String soInvoiceClerk;

    /**
     * 业务员
     */
    private String soSalesMan;

    /**
     * 客户地址
     */
    private String customerAddress;

    /**
     * 客户城市
     */
    private String customerCityName;

    /**
     * 客户区县
     */
    private String customerRegionName;

}
