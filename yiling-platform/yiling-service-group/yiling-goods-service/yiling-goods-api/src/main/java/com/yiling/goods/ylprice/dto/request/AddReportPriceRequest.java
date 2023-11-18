package com.yiling.goods.ylprice.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表参数价格
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddReportPriceRequest extends BaseRequest {

    private static final long serialVersionUID = -6616675196790812941L;
    private Long goodsId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * report_param表id
     */
    private Long paramId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 标准库规格ID
     */
    private Long specificationId;
}
