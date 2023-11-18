package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryB2BOrderCountRequest extends BaseRequest {

    private static final long serialVersionUID = 1718340214637542591L;

    /**
     * 标准库商品IDs
     */
    private List<Long> standardIdList;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 下单开始时间
     */
    private Date startTime;

    /**
     * 下单结束时间
     */
    private Date endTime;

    /**
     * 下单时是否会员 1-非会员 2-是会员
     */
    private Integer vipFlag;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */

    private Integer paymentMethod;

    /**
     * 所属省份编码
     */

    private String provinceCode;

    /**
     *所属城市编码
     */

    private String cityCode;

    /**
     * 所属区域编码
     */

    private String regionCode;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 1-下单企业数 2-供应商数量
     */
    private Integer type;

}
