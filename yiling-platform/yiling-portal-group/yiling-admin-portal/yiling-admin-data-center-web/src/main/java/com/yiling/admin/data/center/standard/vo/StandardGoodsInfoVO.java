package com.yiling.admin.data.center.standard.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

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
public class StandardGoodsInfoVO extends BaseVO {

    private static final long serialVersionUID = -333710312121608L;


    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "注册证号")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 生产地址
     */
    @ApiModelProperty(value = "生产地址")
    private String manufacturerAddress;

    /**
     * 分类
     */
    @ApiModelProperty(value = "分类名称")
    private String standardCategoryName;


    @ApiModelProperty(value = "最后操作时间")
    private Date updateTime;

    @ApiModelProperty(value = "最后操作人")
    private String userName;

    @ApiModelProperty(value = "规格数量")
    private Integer specificationsCount;

    @ApiModelProperty(value = "图片地址")
    private String pic;

    /**
     * 分类名称1
     */
    @ApiModelProperty(value = "分类名称1")
    private String standardCategoryName1;

    /**
     * 分类名称2
     */
    @ApiModelProperty(value = "分类名称2")
    private String standardCategoryName2;

    @ApiModelProperty("标准库商品标签名称列表")
    private List<String> tagNames;
}
