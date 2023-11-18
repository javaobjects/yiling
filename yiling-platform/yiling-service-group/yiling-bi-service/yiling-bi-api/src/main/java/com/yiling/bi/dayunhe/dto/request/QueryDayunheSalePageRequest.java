package com.yiling.bi.dayunhe.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDayunheSalePageRequest extends BaseRequest {

    private String supplierName;

    private String saleOrder;

    private String goodsInSn;

    private String soBatchNo;

    private Integer soQuantity;

    private String goodsName;

    private String soSpecifications;

    /**
     * 销售日期
     */
    private Date startSoTime;

    /**
     * 销售日期
     */
    private Date endSoTime;

}
