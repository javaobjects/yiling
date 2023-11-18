package com.yiling.sjms.crm.form;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateHospitalDrugstoreRelForm extends BaseForm {

    private Long id;

    /**
     * 主流程表单id
     */
    @ApiModelProperty(value = "主流程表单id")
    private Long formId;

    /**
     * 院外药店机构编码
     */
    @NotNull
    @ApiModelProperty(value = "院外药店机构编码")
    private Long drugstoreOrgId;

    /**
     * 院外药店机构名称
     */
    @ApiModelProperty(value = "院外药店机构名称")
    private String drugstoreOrgName;

    /**
     * 医疗机构编码
     */
    @NotNull
    @ApiModelProperty(value = "医疗机构编码")
    private Long hospitalOrgId;

    /**
     * 医疗机构名称
     */
    @ApiModelProperty(value = "医疗机构名称")
    private String hospitalOrgName;

    /**
     * 品种id
     */
    @ApiModelProperty(value = "品种Id")
    private Long categoryId;

    /**
     * 品种名称
     */
    @ApiModelProperty(value = "品种名称")
    private String categoryName;

    /**
     * 标准产品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    @NotNull
    private Long crmGoodsCode;

    /**
     * 开始生效时间
     */
    @ApiModelProperty(value = "开始生效时间")
    private Date effectStartTime;

    /**
     * 结束生效时间
     */
    @ApiModelProperty(value = "结束生效时间")
    private Date effectEndTime;

}
