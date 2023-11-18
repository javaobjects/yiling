package com.yiling.settlement.report.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返利报表的B2B订单同步表
 * </p>
 *
 * @author dexi.yao
 * @date 2022-08-08
 */
@Data
@Accessors(chain = true)
public class B2bOrderSyncBO  {

    private Long id;

    /**
     * 卖家eid
     */
    private Long sellerEid;

    /**
     * 买家eid
     */
    private Long buyerEid;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    private Integer paymentStatus;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品类型,0-普通,1-以岭品
     */
    private Integer orderGoodsType;

    /**
     * 商品类型
     */
    private String goodsType;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 批准文号
     */
    private String license;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

    /**
     * 商品生产厂家
     */
    private String goodsManufacturer;

    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 下单数量
     */
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 退货数量
     */
    private Integer refundQuantity;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 金额小计
     */
    private BigDecimal goodsAmount;

    /**
     * 总折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 总折扣比例
     */
    private BigDecimal discountPercentage;

    /**
     * 支付金额（金额小计-优惠券）
     */
    private BigDecimal paymentAmount;

    /**
     * 活动类型
     */
    private String activityType;

    /**
     * 活动内容
     */
    private String activityDescribe;

    /**
     * 平台承担折扣比例
     */
    private BigDecimal platformPercentage;

    /**
     * 平台承担折扣金额
     */
    private BigDecimal platformAmount;

    /**
     * 商家承担折扣比例
     */
    private BigDecimal shopPercentage;

    /**
     * 商家承担折扣金额
     */
    private BigDecimal shopAmount;

    /**
     * 下单时间
     */
    private Date orderCreateTime;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 报表ID
     */
    private Long reportId;

    /**
     * 报表计算状态：1-未计算 2-已计算
     */
    private Integer reportSettStatus;

    /**
     * 报表状态：0-待返利 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private Integer reportStatus;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    private Integer abnormalReason;

    /**
     * 异常描述
     */
    private String abnormalDescribed;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        B2bOrderSyncBO that = (B2bOrderSyncBO) o;
        return orderNo.equals(that.orderNo) && detailId.equals(that.detailId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNo, detailId);
    }
}
