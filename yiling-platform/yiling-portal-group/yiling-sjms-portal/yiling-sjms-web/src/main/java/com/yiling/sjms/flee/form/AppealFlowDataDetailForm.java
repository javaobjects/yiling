package com.yiling.sjms.flee.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 选择流向申诉数据
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
public class AppealFlowDataDetailForm {

    @ApiModelProperty(value = "申诉数量")
    @NotNull(message = "申诉数量/标准客户名称/标准产品名称/机构属性未填写")
    private BigDecimal appealFinalQuantity;

    @ApiModelProperty("根据变化类型变化的机构名称编码、产品名称编码、机构属性编码")
    @NotNull(message = "申诉数量/标准客户名称/标准产品名称/机构属性未填写")
    private Long changeCode;

    @ApiModelProperty("根据申诉类型变化的机构名称、产品名称、机构属性字段")
    @NotEmpty(message = "申诉数量/标准客户名称/标准产品名称/机构属性未填写")
    private String changeName;

    @ApiModelProperty("根据申诉类型变化的变化类型：1、标准机构名称 2、标准产品名称 3、终端类型")
    @NotNull(message = "申诉数量/标准客户名称/标准产品名称/机构属性未填写")
    private Integer changeType;

    @ApiModelProperty("申诉后产品品规")
    @NotEmpty
    private String appealGoodsSpec;

    @ApiModelProperty("选择流向id")
    @NotEmpty
    private Long id;


}
