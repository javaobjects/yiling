package com.yiling.admin.erp.statistics.vo;

import java.util.Date;

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
public class FlowBalanceStatisticsVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String ename;

    /**
     * 实施负责人
     */
    @ApiModelProperty(value = "实施负责人")
    private String installEmployee;

    /**
     * 统计时间
     */
    @ApiModelProperty(value = "统计时间")
    private Date dateTime;

    /**
     * 采购数量
     */
    @ApiModelProperty(value = "采购数量")
    private Long poQuantity;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "销售数量")
    private Long soQuantity;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Long gbQuantity;

    /**
     * 上一次库存数量
     */
    @ApiModelProperty(value = "上一次库存数量")
    private Long lastGbQuantity;

    /**
     * 相差数量=(上一天库存数量+采购数量-当天库存数量-销售数量)
     */
    @ApiModelProperty(value = "相差数量")
    private Long differQuantity;
}
