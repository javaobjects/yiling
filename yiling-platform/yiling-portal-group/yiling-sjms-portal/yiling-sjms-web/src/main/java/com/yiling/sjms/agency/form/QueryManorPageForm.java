package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryManorPageForm extends QueryPageListForm {
    private static final long serialVersionUID = 1L;

    /**
     * 辖区编码
     */
    @ApiModelProperty("辖区编码")
    private String manorNo;

    /**
     * 辖区名称
     */
    @ApiModelProperty("辖区名称")
    private String name;
    /**
     * 更新日期
     */
    @ApiModelProperty("最后操作时间开始时间")
    private Date beginTime;
    @ApiModelProperty("最后操作时间结束时间")
    private Date endTime;
}
