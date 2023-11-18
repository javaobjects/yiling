package com.yiling.sales.assistant.app.usercustomer.form;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增企业资质 Form
 *
 * @author: lun.yu
 * @date: 2021/9/22
 */
@Data
@ApiModel("新增企业资质")
public class CreateEnterpriseCertificateForm extends BaseForm {

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 资质类型（参见EnterpriseCertificateTypeEnum）
     */
    @ApiModelProperty("资质类型")
    private Integer type;

    /**
     * 资质文件KEY
     */
    @ApiModelProperty("资质文件KEY")
    private String fileKey;

    /**
     * 资质有效期-起
     */
    @ApiModelProperty("资质有效期-起")
    private Date periodBegin;

    /**
     * 资质有效期-止
     */
    @ApiModelProperty("资质有效期-止")
    private Date periodEnd;

    /**
     * 是否长期有效：0-否 1-是
     */
    @ApiModelProperty("是否长期有效：0-否 1-是")
    private Integer longEffective;
}
