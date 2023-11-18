package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.Map;

import com.yiling.dataflow.order.dto.StorageInfoDTO;
import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/7/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FlowPurchaseGoodsMonthVO extends BaseVO {

    private Long specificationId;

    private String spec;

    private String goodsName;

    private Map<String, StorageInfoVO> storageInfoMap;

    private BigDecimal storageQuantitySum;

    private Integer rank;
}
