package com.yiling.dataflow.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流向商品库存日志
 * </p>
 *
 * @author houjie.sun
 * @date 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_purchase_inventory_log")
public class FlowPurchaseInventoryLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：flow-erp流向库存 settlement-报表计算库存
     */
    private String businessType;

    /**
     * 流向商品库存id
     */
    private Long flowPurchaseInventoryId;

    /**
     * 采购来源：1-大运河 2-京东
     */
    private Integer poSource;

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 商业商品内码
     */
    private String goodsInSn;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 变更前库存数量
     */
    private BigDecimal beforeQuantity;

    /**
     * 变更数量
     */
    private BigDecimal changeQuantity;

    /**
     * 变更后库存数量
     */
    private BigDecimal afterQuantity;

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
     * 备注
     */
    private String remark;


}
