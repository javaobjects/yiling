package com.yiling.b2b.app.common.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppBannerForm extends BaseForm {

    @ApiModelProperty(value = "企业id", required = true)
    @NotNull(message = "企业不能为空")
    private Long eid;

    @ApiModelProperty(value = "店铺banner的轮播数量", required = true)
    @NotNull(message = "轮播数量不能为空")
    @Range(message = "轮播数量范围为 {min} 到 {max} 之间", min = 1, max = 10)
    private Integer count;
}
