package com.yiling.dataflow.flowcollect.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 月流向库存上传数据表
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_month_inventory")
public class FlowMonthInventoryDO extends BaseDO {

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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
