package com.yiling.order.order.bo;

import lombok.Builder;
import lombok.Data;

/** 组合包活动购买数量BO
 * @author zhigang.guo
 * @date: 2022/12/14
 */
@Data
@Builder
public class CombinationBuyNumberBO implements java.io.Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 用户当天购买数量
     */
    private Long buyerDayQty;

    /**
     * 总计购买数量
     */
    private Long sumQty;

    /**
     * 用户购买数量
     */
    private Long buyerQty;
}
