package com.yiling.f2b.admin.agreementv2.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询指定商业公司 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-09
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBusinessPageForm extends QueryPageListForm {


    /**
     * 公司名称
     */
    @Length(max = 50)
    @ApiModelProperty("公司名称")
    private String ename;

}
