package com.yiling.sjms.flow.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/6
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("流向接口监控-统计未上传流向企业列表VO")
public class FlowMonitorNoDataSyncEnterpriseVO extends BaseVO {

    /**
     * 企业id
     */
    @ApiModelProperty(value = "经销商编码", example = "1")
    private Long rkSuId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "经销商名称", example = "2")
    private String clientName;

    /**
     * 流向收集方式
     */
    @ApiModelProperty(value = "流向收集方式", example = "3")
    private Integer flowMode;

    /**
     * 未上传流向天数
     */
    @ApiModelProperty(value = "未上传流向天数", example = "4")
    private Integer noDataSyncDaysCount;

}
