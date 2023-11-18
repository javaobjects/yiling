package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author fucheng.bai
 * @date 2022/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StorageInfoDTO extends BaseDTO {

    private String time;

    private BigDecimal storageMoney;

    private BigDecimal storageQuantity;

    private Integer rank;


    public StorageInfoDTO(String time, BigDecimal storageMoney, BigDecimal storageQuantity) {
        this.time = time;
        this.storageMoney = storageMoney;
        this.storageQuantity = storageQuantity;
    }
}
