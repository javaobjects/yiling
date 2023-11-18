package com.yiling.b2b.app.promotion.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shixing.sun
 * @date 2022-05-06
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SpecialActivityInfoVO {
    @ApiModelProperty(value = "活动数量")
    private Integer size;

    @ApiModelProperty(value = "起配额度")
    private BigDecimal minSize;

    @ApiModelProperty(value = "是否建材0 没有 1已经建采")
    private Integer contract;

    @ApiModelProperty(value = "企业id")
    private Long eid;

    @ApiModelProperty(value = "企业名称")
    private String enterpriseName;

    @ApiModelProperty(value = "商户下面的专场活动信息")
    private List<SpecialActivityItemInfoVO> activityInfo;
}
