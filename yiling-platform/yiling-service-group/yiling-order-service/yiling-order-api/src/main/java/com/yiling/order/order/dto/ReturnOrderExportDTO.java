package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退货单信息导出字段
 *
 * @author: yong.zhang
 * @date: 2021/8/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReturnOrderExportDTO extends BaseDTO {
    /**
     * 退货单单据编号
     */
    private String     returnNo;
    /**
     * 订单编号
     */
    private String     orderNo;
    /**
     * 退货单单据类型
     */
    private String     returnType;
    /**
     * 退货单单据状态
     */
    private String     returnStatus;
    /**
     * 单据提交时间
     */
    private Date       createTime;
    /**
     * 支付方式
     */
    private String     paymentMethod;
    /**
     * 退款总金额(元)
     */
    private BigDecimal totalReturnAmount;
    /**
     * 商品ID
     */
    private String     goodsId;
    /**
     * 商品名称
     */
    private String     goodsName;
    /**
     * 规格型号
     */
    private String     goodsSpecification;
    /**
     * 退货数量
     */
    private Integer    goodsReturnQuality;
    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;
    /**
     * 退货金额
     */
    private BigDecimal goodsReturnAmount;
    /**
     * 折扣金额
     */
    private BigDecimal goodsDiscountAmount;
    /**
     * 退款金额
     */
    private BigDecimal returnAmounts;
    /**
     * 批次号/序列号
     */
    private String     batchNo;
    /**
     * 有效期至
     */
    private Date       expiryDate;
    /**
     * 退货数量
     */
    private Integer    returnQuality;

    /**
     * 导出需要的字符串创建时间
     */
    private String createdTime;
    /**
     * 导出需要的字符串有效期至
     */
    private String expiredDate;

    public String getCreatedTime() {
        if (null == createTime) {
            return DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        }
        return DateUtil.format(createTime, "yyyy-MM-dd HH:mm:ss");
    }

    public String getExpiredDate() {
        if (null == expiryDate) {
            return DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        }
        return DateUtil.format(expiryDate, "yyyy-MM-dd HH:mm:ss");
    }
}
