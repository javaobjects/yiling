package com.yiling.admin.hmc.welfare.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hongyang.zhang
 * @data: 2022/10/08
 */
@Data
public class DrugWelfareCouponStatisticsForm extends BaseForm {

    @NotNull
    @ApiModelProperty("组id集合")
    private List<Long> groupIds;

}
