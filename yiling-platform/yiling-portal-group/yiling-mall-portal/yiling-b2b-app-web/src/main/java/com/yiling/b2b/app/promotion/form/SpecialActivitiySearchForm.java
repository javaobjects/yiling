package com.yiling.b2b.app.promotion.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2022/05/24
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SpecialActivitiySearchForm {
    @ApiModelProperty("企业名称")
    private String ename;

    @ApiModelProperty("活动类型 1-满赠，2-特价，3-秒杀, 4-组合包")
    private Integer type;

    @ApiModelProperty("营销活动id")
    private Long promotionActivityId;

    @ApiModelProperty("产品名称")
    private String goodsName;

    @ApiModelProperty("商户id-卖家id")
    private Long eid;

    @ApiModelProperty(value = "每页显示条数，默认10", example = "10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页，默认1", example = "1")
    private Integer current = 1;
}
