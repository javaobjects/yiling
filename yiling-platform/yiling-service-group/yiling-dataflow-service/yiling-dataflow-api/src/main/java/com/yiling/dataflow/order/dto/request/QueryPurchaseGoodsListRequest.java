package com.yiling.dataflow.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPurchaseGoodsListRequest extends BaseRequest {

    // 年月
    private String time;

    // 采购商业列表
    private List<Long> purchaseEnterpriseIds;

    // 采购商品名称列表
    private List<String> goodsNameList;

    // 最小入库数量
    private Integer minQuantity;

    // 最大入库数量
    private Integer maxQuantity;

    // 省编码
    private String provinceCode;

    // 市编码
    private String cityCode;

    // 区编码
    private String regionCode;

    private List<String> poMonthList;
}
