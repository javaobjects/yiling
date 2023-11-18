package com.yiling.sjms.agency.form;


import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * 保存辖区基本信息form
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmManorRepresentativeForm extends BaseForm {
    private Long id;
    /**
     * 辖区编码
     */
    @ApiModelProperty(value = "辖区ID")
    private Long manorId;
    /**
     * 代表岗位编码
     */
    @ApiModelProperty(value = "代表岗位编码")
    private Long representativePostCode;

    /**
     * 代表岗位名称
     */
    @ApiModelProperty(value = "代表岗位名称")
    private String representativePostName;
    /**
     * 上级主管岗位id
     */
    @ApiModelProperty("上级主管岗位id")
    private Long superiorJob;

    /**
     * 职级编码
     */
    @ApiModelProperty("职级编码")
    private String dutyGredeId;

    /**
     * 上级主管岗位名称
     */
    @ApiModelProperty("上级主管岗位名称")
    private String superiorJobName;

}
