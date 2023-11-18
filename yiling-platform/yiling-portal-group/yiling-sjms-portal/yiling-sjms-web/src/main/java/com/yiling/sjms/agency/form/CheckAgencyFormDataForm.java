package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/2/28 0028
 */
@Data
public class CheckAgencyFormDataForm extends BaseForm {

    /**
     * 表单id-修改时需要
     */
    @ApiModelProperty("表单id-修改时校验需要")
    private Long id;

    /**
     * 校验的机构的id
     */
    @ApiModelProperty("校验的机构的id")
    private Long crmEnterpriseId;

    /**
     * 主流程表单主表id
     */
    @ApiModelProperty("主流程表单主表id")
    private Long formId;
    
    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    @ApiModelProperty("供应链角色：1-商业公司 2-医疗机构 3-零售机构")
    private Integer supplyChainRole;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;
}
