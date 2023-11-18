package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderExportReportPageRequest extends QueryPageListRequest {

    /**
     * 下单开始时间
     */
    private Date startCreatTime;

    /**
     * 下单结束时间
     */
    private Date endCreatTime;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 商品品类
     */
    private List<String> categoryList;

    /**
     * 支付方式
     */
    private List<Long> paymentMethodList;

    /**
     * 采购供应类型 1 采购商 2 供应商
     */
    private Integer type;

    /**
     * 数据类型 1:POP购进总数据 2:POP回款总数据 3:B2B自建平台总数据 4:B2B大运河动销总数据
     */
    private Integer recordType;

    /**
     * 商品品类类型( 1:莲花 2:非莲花)
     */
    private Integer categoryType;

    /**
     * 企业eid
     */
    private List<Long> eidList;
}
