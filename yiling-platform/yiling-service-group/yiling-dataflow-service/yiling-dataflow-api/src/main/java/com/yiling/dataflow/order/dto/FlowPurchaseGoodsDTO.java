package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FlowPurchaseGoodsDTO extends BaseDTO {

    private Long specificationId;

    private String spec;

    private String goodsName;

    private Map<String, StorageInfoDTO> storageInfoMap;

    private BigDecimal storageQuantitySum;

    private Integer rank;
}
