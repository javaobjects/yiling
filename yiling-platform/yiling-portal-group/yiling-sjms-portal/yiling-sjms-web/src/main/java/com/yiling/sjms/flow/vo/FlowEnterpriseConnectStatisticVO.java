package com.yiling.sjms.flow.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectStatisticVO
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Data
public class FlowEnterpriseConnectStatisticVO {
    /**
     * 所属省份编码
     */
    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "所属省份名称")
    private String provinceName;

    /**
     * 经销商数量
     */
    @ApiModelProperty(value = "经销商数量")
    private Integer enterpriseCount;

    /**
     * 无效连接数量
     */
    @ApiModelProperty(value = "无效连接数量")
    private Integer invalidCount;

    /**
     * 有效连接数量
     */
    @ApiModelProperty(value = "有效连接数量")
    private Integer validCount;

    /**
     * 无效占比
     */
    @ApiModelProperty(value = "无效占比")
    private BigDecimal invalidRatio;
}
