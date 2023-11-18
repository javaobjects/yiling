package com.yiling.sjms.goodsplans.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StockForecastInfoForm {
    @ApiModelProperty(value = "经销商eid")
    private String eid;
    @ApiModelProperty(value = "经销商体系名称")
    private String enameGroup;
    @ApiModelProperty(value = "经销商等级名称")
    private String enameLevel;
    @ApiModelProperty(value = "商品规格ID")
    private Long specificationId;
    @ApiModelProperty(value = "补货天数")
    private Long replenishDays;
    @ApiModelProperty(value = "参考时间1")
    private Long referenceTime1;
    @ApiModelProperty(value = "参考时间2")
    private Long referenceTime2;
    @ApiModelProperty(value = "商业级别")
    private Integer supplierLevel;
    /**
     * 商业体系
     */
    @ApiModelProperty(value = "商业体系")
    private Integer businessSystem;
    @ApiModelProperty(value = "机构编码")
    private Long crmEnterpriseId;
}
