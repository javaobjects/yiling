package com.yiling.sjms.flow.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveFlowEnterpriseGoodsMappingForm
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowEnterpriseGoodsMappingForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * crm商品编码
     */
    @NotNull
    @ApiModelProperty(value = "标准商品编码")
    private Long crmGoodsCode;

    /**
     * 标准商品名称
     */
    @NotEmpty
    @ApiModelProperty(value = "标准商品名称")
    private String goodsName;

    /**
     * 转换单位：1-乘 2-除
     */
    @ApiModelProperty(value = "转换单位：1-乘 2-除")
    private Integer convertUnit;

    /**
     * 转换系数
     */
    @ApiModelProperty(value = "转换系数")
    private BigDecimal convertNumber;
}
