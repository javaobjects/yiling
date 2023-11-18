package com.yiling.hmc.medinstruct.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsInfoVO extends BaseVO {

    private static final long serialVersionUID = -333710312121608L;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "批准文号/生产许可证号")
    private String licenseNo;

    @ApiModelProperty(value = "图片地址")
    private String pic;

}
