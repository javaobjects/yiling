package com.yiling.bi.order.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bi_order_backup")
public class BiOrderBackupDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 备份之前的order ID
     */
    private Long odsDetailId;

    /**
     * 备份任务id
     */
    private Long taskId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 买家名称
     */
    private String buyerEname;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 卖家名称
     */
    private String sellerEname;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 退货数量
     */
    private Integer returnQuantity;

    /**
     * 原入库时间
     */
    private Date extractTime;

    /**
     * 是否已匹配 0-未匹配 1-已匹配
     */
    private Integer matchFlag;

    /**
     * 匹配CRM流向数据ID，多个以逗号隔开
     */
    private String matchCrmId;

    /**
     * 否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;
}
