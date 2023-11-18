package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateManorRelation extends BaseForm {
    @ApiModelProperty("ID")
    private Long id;
    /**
     * 辖区表Id
     */
    @NotNull
    @ApiModelProperty("辖区表Id")
    private Long crmManorId;

    /**
     * 机构编码
     */
    @ApiModelProperty("机构编码")
    @NotNull
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String crmEnterpriseName;
    /**
     * 、
     * 机构Crm编码
     */
    private String crmEnterpriseCode;


    /**
     * 品类ID
     */
    @NotNull
    @ApiModelProperty("品类ID")
    private Long categoryId;

    /**
     * 品类名称
     */
    @ApiModelProperty("品类名称")
    private String categoryName;
}
