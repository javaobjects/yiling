package com.yiling.dataflow.check.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_purchase_check_task")
public class FlowPurchaseCheckTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * job任务ID
     */
    private Long checkJobId;

    /**
     * 采购企业ID
     */
    private Long eid;

    /**
     * 采购企业名称
     */
    private String ename;

    /**
     * 供应商eid
     */
    private Long supplierId;

    /**
     * 采购时间
     */
    private Date poTime;

    /**
     * 商品规格id
     */
    private Long specificationId;

    /**
     * 采购时间
     */
    private String poBatchNo;

    /**
     * 采购数量小计
     */
    private BigDecimal totalPoQuantity;

    /**
     * 销售数量小计
     */
    private BigDecimal totalSoQuantity;

    /**
     * 有无销售：1-无销售，2-有销售-数量不符合(采购数量>销售数量)，3-有销售-数量符合（字典：erp_purchase_sale_flag）
     */
    private Integer saleFlag;

    /**
     * 核查状态，0未核查，1正在核查 2核查成功 3核查失败
     */
    private Integer checkStatus;

    /**
     * 核查信息
     */
    private String checkMsg;

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
