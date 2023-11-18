package com.yiling.dataflow.report.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveReportFlowGoodsBatchRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 商业公司id
     */
    private Long eid;

    /**
     * 商业公司名称
     */
    private String ename;

    /**
     * 商品品类:0连花1非连花
     */
    private Integer goodsCategory;

    /**
     * 销售时间
     */
    private Date gbTime;

    /**
     * 销售数量
     */
    private Long gbQuantity;

    /**
     * 商销价
     */
    private BigDecimal ylSalePrice;

    /**
     * 销售总额
     */
    private BigDecimal totalAmount;
}
