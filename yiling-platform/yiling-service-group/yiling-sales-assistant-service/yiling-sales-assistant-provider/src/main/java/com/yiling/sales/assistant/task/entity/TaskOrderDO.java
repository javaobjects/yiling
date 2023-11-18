package com.yiling.sales.assistant.task.entity;

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
 * 任务订单
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_order")
public class TaskOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 任务主键
     */
    private Long taskId;

    /**
     * 终端id enterprise表主键
     */
    private Long terminalId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 实收数量
     */
    private Integer realAmount;

    /**
     * 商品数量
     */
    private Integer amount;

    /**
     * 总价格
     */
    private BigDecimal totalMoney;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 实收金额
     */
    private BigDecimal realMoney;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 下单时间
     */
    private Date orderTime;

    private Long userTaskId;


}
