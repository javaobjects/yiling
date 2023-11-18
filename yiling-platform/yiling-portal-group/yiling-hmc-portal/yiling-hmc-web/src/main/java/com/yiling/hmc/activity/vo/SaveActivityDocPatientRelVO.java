package com.yiling.hmc.activity.vo;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存医带患关系VO
 * @author: fan.shen
 * @date: 2023-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveActivityDocPatientRelVO extends BaseForm {

    @ApiModelProperty("1-患者已被其他用户绑定，无需重复绑定，2-患者通过问诊已绑定，无需重复绑定，3-添加成功")
    private Integer flag;

}