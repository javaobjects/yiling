package com.yiling.sjms.wash.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateUnlockThirdRecordForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    @NotNull(message = "客户机构编码不可为空")
    @ApiModelProperty(value = "客户机构编码")
    private Long orgCrmId;

    @NotNull(message = "月购进额度不可为空")
    @ApiModelProperty(value = "月购进额度")
    private BigDecimal purchaseQuota;

    @ApiModelProperty(value = "部门信息列表")
    private List<DepartmentInfo> departmentInfoList;

    @ApiModelProperty(value = "备注")
    private String remark;

    @Data
    public static class DepartmentInfo {

        @ApiModelProperty(value = "部门编号")
        private String code;

        @ApiModelProperty(value = "部门名称")
        private String name;
    }
}
