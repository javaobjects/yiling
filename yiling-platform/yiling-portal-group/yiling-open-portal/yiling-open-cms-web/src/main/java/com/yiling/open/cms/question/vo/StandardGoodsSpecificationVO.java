package com.yiling.open.cms.question.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsSpecificationVO extends BaseDTO {

    private static final long serialVersionUID = -333712190231608L;

    /**
     * 标准商品ID
     */
    @ApiModelProperty("标准商品ID")
    private Long standardId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 注册证号
     */
    @ApiModelProperty("注册证号")
    private String licenseNo;



    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;

}
