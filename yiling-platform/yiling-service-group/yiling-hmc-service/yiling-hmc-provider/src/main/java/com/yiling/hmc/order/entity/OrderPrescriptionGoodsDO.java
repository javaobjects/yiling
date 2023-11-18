package com.yiling.hmc.order.entity;

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
 * <p>
 * C端兑付订单关联处方商品表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_order_prescription_goods")
public class OrderPrescriptionGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 兑付订单id
     */
    private Long orderId;

    /**
     * 兑付订单处方id
     */
    private Long orderPrescriptionId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 数量
     */
    private Integer goodsQuantity;

    /**
     * 用法用量
     */
    private String usageInfo;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;


}
