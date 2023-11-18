package com.yiling.data.center.admin.enterprise.form;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业资质副本信息 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class EnterpriseCertificateAuthInfoForm extends BaseForm {

    /**
     * 资质类型（参见EnterpriseCertificateTypeEnum）
     */
    @ApiModelProperty("资质类型（参见EnterpriseCertificateTypeEnum）")
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
