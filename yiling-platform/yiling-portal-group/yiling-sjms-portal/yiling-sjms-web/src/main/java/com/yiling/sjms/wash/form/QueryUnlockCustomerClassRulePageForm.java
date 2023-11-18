package com.yiling.sjms.wash.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUnlockCustomerClassRulePageForm extends QueryPageListForm {

    @ApiModelProperty(value = "规则id")
    private Long ruleId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "非锁客户分类 1-零售机构 2-商业公司 3-医疗机构 4-政府机构")
    private Integer customerClassification;

    @ApiModelProperty(value = "状态 1-开启 0-关闭")
    private Integer status;

    @ApiModelProperty(value = "最后操作开始时间")
    private Date startOpTime;

    @ApiModelProperty(value = "最后操作结束时间")
    private Date endOpTime;
}
