package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowPurchaseDetailVO extends BaseVO {

    @ApiModelProperty("采购商Id")
    private Long purchaseEnterpriseId;

    @ApiModelProperty("采购商名称")
    private String purchaseEnterpriseName;

    @ApiModelProperty("渠道类型id")
    private Long channelId;

    @ApiModelProperty("渠道类型")
    private String channelDesc;

    @ApiModelProperty("明细表")
    private List<StorageInfoVO> storageInfoList;


}
