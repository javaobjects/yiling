package com.yiling.admin.erp.statistics.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowBalanceStatisticsMonthVO extends BaseVO {

    @ApiModelProperty(value = "年月份")
    private String time;

    @ApiModelProperty(value = "企业id")
    private Long eid;

    @ApiModelProperty(value = "企业名称")
    private String ename;

    @ApiModelProperty(value = "实施负责人")
    private String installEmployee;

    @ApiModelProperty(value = "采购数量")
    private Long poQuantity;

    @ApiModelProperty(value = "未匹配采购数量")
    private Long noMatchPoQuantity;

    @ApiModelProperty(value = "采购上月增长率")
    private String poGrowthRate;

    @ApiModelProperty(value = "销售数量")
    private Long soQuantity;

    @ApiModelProperty(value = "未匹配销售数量")
    private Long noMatchSoQuantity;

    @ApiModelProperty(value = "销售上月增长率")
    private String soGrowthRate;

    @ApiModelProperty(value = "当前库存量")
    private Long gbQuantity;

    @ApiModelProperty(value = "未匹配库存数量")
    private Long noMatchGbQuantity;

    @ApiModelProperty(value = "月初库存量")
    private Long beginMonthQuantity;

    @ApiModelProperty(value = "平衡相差数")
    private Long differQuantity;
}
