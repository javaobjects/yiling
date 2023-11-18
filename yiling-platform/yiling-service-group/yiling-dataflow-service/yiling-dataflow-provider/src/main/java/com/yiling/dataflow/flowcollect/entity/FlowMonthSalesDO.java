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
 * 月流向销售上传数据表
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_month_sales")
public class FlowMonthSalesDO extends BaseDO {

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
