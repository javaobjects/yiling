package com.yiling.admin.b2b.member.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买条件 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@ApiModel("会员购买条件")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateMemberBuyStageForm extends BaseForm {

    /**
     * 价格
     */
    @NotNull
    @ApiModelProperty(value = "价格", required = true)
    private BigDecimal price;

    /**
     * 有效时长
     */
    @NotNull
    @ApiModelProperty(value = "有效时长", required = true)
    private Integer validTime;

    /**
     * 名称（如：季卡VIP）
     */
    @NotEmpty
    @Length(max = 5)
    @ApiModelProperty(value = "名称（如：季卡VIP）", required = true)
    private String name;

    /**
     * 标签名称
     */
    @Length(max = 10)
    @ApiModelProperty("标签名称")
    private String tagName;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

}
