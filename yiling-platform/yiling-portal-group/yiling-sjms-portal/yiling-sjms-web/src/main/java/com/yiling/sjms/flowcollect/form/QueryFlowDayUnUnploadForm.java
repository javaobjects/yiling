package com.yiling.sjms.flowcollect.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowDayUnUnploadForm extends BaseForm {
    @ApiModelProperty("id")
    private Long id;

    /**
     * 商业公司编码
     */
    @ApiModelProperty("机构编码")
    private Long crmEnterpriseId;

    /**
     * 统计的时间
     */
    @ApiModelProperty("未上传的时间 格式 yyyy-mm-dd")
    private Date statisticsTime;
}
