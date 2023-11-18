package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucheng.bai
 * @date 2022/7/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageInfoVO {

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("金额")
    private BigDecimal storageMoney;

    @ApiModelProperty("数量")
    private BigDecimal storageQuantity;

    @ApiModelProperty("排名")
    private Integer rank;
}
