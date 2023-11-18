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
public class StandardGoodVO extends BaseDTO {

    private static final long serialVersionUID = -33371030421231608L;


    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 批准文号
     */
    @ApiModelProperty("批准文号")
    private String licenseNo;

    /**
     * 以岭标识 0:非以岭  1：以岭
     */
    @ApiModelProperty("以岭标识 0:非以岭  1：以岭")
    private Integer ylFlag;


    /**
     * 默认图片信息
     */
    @ApiModelProperty("默认图片信息")
    private String pic;

}
