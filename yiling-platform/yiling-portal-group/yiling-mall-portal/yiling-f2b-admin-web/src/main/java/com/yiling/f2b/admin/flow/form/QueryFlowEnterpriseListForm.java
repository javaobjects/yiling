package com.yiling.f2b.admin.flow.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowEnterpriseListForm extends BaseForm {

    /**
     * 商业名称（模糊查询）
     */
    @ApiModelProperty(value = "商业名称（模糊查询）")
    private String ename;

}
