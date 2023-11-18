package com.yiling.open.cms.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author hongyang.zhang
 * @date 2022/06/07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsInfoVO extends BaseVO {

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("标准库ID")
    private Long standardId;

    @ApiModelProperty("售卖规格ID")
    private Long sellSpecificationsId;

    @ApiModelProperty("销售规格")
    private String specifications;

    @ApiModelProperty("售卖规格")
    private String sellSpecifications;

    @ApiModelProperty("商品图片值")
    private String pic;


}
