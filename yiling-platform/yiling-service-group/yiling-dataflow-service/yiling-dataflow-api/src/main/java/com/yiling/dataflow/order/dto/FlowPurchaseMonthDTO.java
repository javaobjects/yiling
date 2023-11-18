package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowPurchaseMonthDTO extends BaseDTO {

    private Long purchaseEnterpriseId;

    private String purchaseEnterpriseName;

    private Long purchaseChannelId;

    private String purchaseChannelDesc;

    private Long supplierEnterpriseId;

    private String supplierEnterpriseName;

    private Long supplierChannelId;

    private String supplierChannelDesc;

    private Map<String, StorageInfoDTO> storageInfoMap;

    private BigDecimal storageMoneySum;

    private BigDecimal storageQuantitySum;

}
