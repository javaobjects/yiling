package com.yiling.open.web.vo;

import java.math.BigDecimal;
import java.util.Objects;

import com.yiling.order.order.dto.OrderTicketDiscountDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 订单票折记录表，按照订单id和票折单号统计
 * @author:dexi.yao
 * @date:2021/7/2
 */
@Data
@Accessors(chain = true)
public class OrderTicketDiscountVO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 票折单号
     */
    private String ticketDiscountNo;

	/**
	 * ERP出库单号
	 */
	private String erpDeliveryNo;

	/**
	 * ERP应收单号
	 */
	private String erpReceivableNo;


	@Override
	public boolean equals(Object o) {
		if (this == o){
			return true;
		}
		if (!(o instanceof OrderTicketDiscountDTO)){
			return false;
		}
		if (!super.equals(o)){
			return false;
		}
		OrderTicketDiscountDTO that = (OrderTicketDiscountDTO) o;
		return orderId.equals(that.getOrderId()) &&
				orderNo.equals(that.getOrderNo());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), orderId, orderNo);
	}

}
