package com.yiling.dataflow.flow.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_purchase_sales_inventory")
public class FlowPurchaseSalesInventoryDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long eid;
    /**
     * 年份(计入)
     */
    private String year;

    /**
     * 月份(计入)
     */
    private String month;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 产品品种
     */
    private String breed;

    /**
     * 产品编码
     */
    private Long crmGoodsId;

    /**
     * 产品品名
     */
    private String goodsName;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 产品考核价
     */
    private BigDecimal sxPrice;

    /**
     * 上月存/盒
     */
    private BigDecimal lastNumber;

    /**
     * 本月进/盒
     */
    private BigDecimal purchaseNumber;

    /**
     * 本月销/盒
     */
    private BigDecimal saleNumber;

    /**
     * 本月实际库存/盒
     */
    private BigDecimal number;

    /**
     * 本月在途库存/盒
     */
    private BigDecimal onWayNumber;

    /**
     * 本月库存合计/盒
     */
    private BigDecimal totalNumber;

    /**
     * 计算本月存/盒
     */
    private BigDecimal calculationNumber;

    /**
     * 库存差异/盒
     */
    private BigDecimal diffNumber;

    /**
     * 上月存/元
     */
    private BigDecimal lastNumberAmount;

    /**
     * 本月进/元
     */
    private BigDecimal purchaseNumberAmount;

    /**
     * 本月销/元
     */
    private BigDecimal saleNumberAmount;

    /**
     * 本月实际库存/元
     */
    private BigDecimal numberAmount;

    /**
     * 本月在途库存/元
     */
    private BigDecimal onWayNumberAmount;

    /**
     * 本月库存合计/元
     */
    private BigDecimal totalNumberAmount;

    /**
     * 计算本月存/元
     */
    private BigDecimal calculationNumberAmount;

    /**
     * 库存差异/元
     */
    private BigDecimal diffNumberAmount;

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
