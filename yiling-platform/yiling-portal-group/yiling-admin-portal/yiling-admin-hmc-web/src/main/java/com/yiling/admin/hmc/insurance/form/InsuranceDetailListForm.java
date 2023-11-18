package com.yiling.admin.hmc.insurance.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceDetailListForm extends QueryPageListForm {

    @ApiModelProperty("商家id")
    private Long eid;
    
    @ApiModelProperty("保险名称")
    private String InsuranceName;
}
