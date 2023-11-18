package com.yiling.sjms.wash.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QueryUnlockThirdRecordPageForm extends QueryPageListForm {

    /**
     * 客户机构编码
     */
    @ApiModelProperty(value = "客户机构编码")
    private Long orgCrmId;

    /**
     * 操作开始时间
     */
    @ApiModelProperty(value = "操作开始时间")
    private Date opStartTime;

    /**
     * 操作结束时间
     */
    @ApiModelProperty(value = "操作结束时间")
    private Date opEndTime;
}
