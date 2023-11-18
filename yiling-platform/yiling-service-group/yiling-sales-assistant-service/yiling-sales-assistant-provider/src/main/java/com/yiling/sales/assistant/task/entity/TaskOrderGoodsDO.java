package com.yiling.sales.assistant.task.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务商品
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_order_goods")
public class TaskOrderGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务订单表主键
     */
    private Long taskOrderId;

    /**
     * 商品主键
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 商品数量
     */
    private Integer amount;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 逻辑删除
     */
    private Integer isDelete;

    /**
     * 实收数量
     */
    private Integer realAmount;

    /**
     * 实收金额
     */
    private BigDecimal realMoney;


}
