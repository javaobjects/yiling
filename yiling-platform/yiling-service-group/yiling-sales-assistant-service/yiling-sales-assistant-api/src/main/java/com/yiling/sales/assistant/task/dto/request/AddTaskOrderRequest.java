package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *   任务订单添加接口实体
 * </p>
 *
 * @author gxl
 * @since 2020-04-25
 */
@Data
@Accessors(chain = true)
public class AddTaskOrderRequest implements Serializable {

    private static final long serialVersionUID = 2992296163405573070L;


    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 终端id(下单时选择的客户)
     */
    private Long terminalId;



    /**
     * 用户名
     */
    private String userName;



    /**
     * 订单编号
     */
    private String     orderNo;
    /**
     * 收货总金额
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;
    /**
     * 是否首单（销售助手下的订单）
     */
    private Boolean    isFirstOrder;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 终端(下单时选择的客户名称)
     */
    private String terminalName;

    /**
     * 订单详情
     */
    private List<AddTaskOrderGoodsRequest> orderGoodsAddDTOList;


    private List<Long> goodsIds;


    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 是否数据修复
     */
    private boolean isDataFix;

    /**
     * 支付方式：1-线下支付 2-账期 4-在线支付
     */
    private Integer paymentMethod;
}
