package com.yiling.admin.b2b.presale.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddPresaleActivityForm extends BaseForm {

    @ApiModelProperty("活动id-修改时需要")
    private Long id;

    @ApiModelProperty("活动名称")
    @NotBlank(message = "活动名称不能为空")
    private String name;

    @ApiModelProperty("活动分类（1-平台活动；2-商家活动；）")
    @NotNull(message = "活动分类不能为空")
    private Integer sponsorType;

    @ApiModelProperty("生效开始时间")
    @NotNull(message = "生效开始时间不能为空")
    private Date beginTime;

    @ApiModelProperty("生效结束时间")
    @NotNull(message = "生效结束时间不能为空")
    private Date endTime;

    @ApiModelProperty("运营备注")
    private String operatingRemark;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    @ApiModelProperty(value = "费用承担方（1-平台；2-商家；3-共同承担）")
    private Integer bear;

    /**
     * 平台承担比例
     */
    @ApiModelProperty(value = "平台承担比例")
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    @ApiModelProperty(value = "商家承担比例")
    private BigDecimal businessRatio;
}
