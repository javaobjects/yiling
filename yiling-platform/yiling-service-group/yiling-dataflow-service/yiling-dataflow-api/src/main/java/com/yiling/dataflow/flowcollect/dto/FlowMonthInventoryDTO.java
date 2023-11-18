package com.yiling.dataflow.flowcollect.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 月流向库存上传数据表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowMonthInventoryDTO extends BaseDTO {

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
