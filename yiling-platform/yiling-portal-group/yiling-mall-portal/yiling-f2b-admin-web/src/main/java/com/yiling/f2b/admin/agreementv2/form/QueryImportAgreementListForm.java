package com.yiling.f2b.admin.agreementv2.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议导入列表查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-16
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryImportAgreementListForm extends BaseForm {

    /**
     * 甲方名称
     */
    @ApiModelProperty(value = "甲方名称")
    private String ename;


    /**
     * 乙方名称
     */
    @ApiModelProperty(value = "乙方名称")
    private String secondName;
}
