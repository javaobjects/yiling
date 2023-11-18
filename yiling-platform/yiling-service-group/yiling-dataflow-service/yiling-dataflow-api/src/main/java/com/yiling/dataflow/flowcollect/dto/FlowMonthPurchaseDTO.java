package com.yiling.dataflow.flowcollect.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 月流向采购上传数据表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowMonthPurchaseDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

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

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
