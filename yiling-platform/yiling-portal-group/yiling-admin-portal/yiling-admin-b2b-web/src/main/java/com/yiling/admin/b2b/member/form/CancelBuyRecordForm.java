package com.yiling.admin.b2b.member.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-取消导入购买记录 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-02
 */
@Data
@ApiModel("取消导入购买记录Form")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CancelBuyRecordForm extends BaseForm {

    /**
     * 购买记录ID
     */
    @NotNull
    @ApiModelProperty(value = "购买记录ID", required = true)
    private Long id;

}
