package com.yiling.sjms.wash.form;

import java.util.Date;

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
public class QueryUnlockCustomerClassDetailPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "经销商crmId")
    private Long crmEnterpriseId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "是否分类 0-未分类 1-已分类")
    private Integer classFlag;

    @ApiModelProperty(value = "非锁客户分类 1-零售机构 2-商业公司 3-医疗机构 4-政府机构")
    private Integer customerClassification;

    @ApiModelProperty(value = "最后操作开始时间")
    private Date startOpTime;

    @ApiModelProperty(value = "最后操作结束时间")
    private Date endOpTime;
}
